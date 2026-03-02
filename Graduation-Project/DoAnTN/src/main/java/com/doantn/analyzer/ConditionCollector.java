package com.doantn.analyzer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ConditionCollector: visitor that collects branch conditions (if/switch/for/foreach)
 * and attempts to decompose expressions into simple atomic constraints.
 *
 * This is intentionally conservative and produces string-based constraints which can
 * later be enriched by SymbolSolver to obtain types and declarations.
 */
public class ConditionCollector extends VoidVisitorAdapter<List<ConditionCollector.BranchCondition>> {

    @Override
    public void visit(IfStmt n, List<ConditionCollector.BranchCondition> collector) {
        super.visit(n, collector);
        String condition = n.getCondition().toString();
        int line = n.getBegin().map(p -> p.line).orElse(-1);
        String method = findEnclosingMethodName(n);
        BranchCondition bc = new BranchCondition(BranchCondition.Kind.IF, condition, line, method);
        bc.atomicConstraints.addAll(decomposeExpression(n.getCondition()));
        collector.add(bc);
    }

    @Override
    public void visit(SwitchStmt n, List<ConditionCollector.BranchCondition> collector) {
        super.visit(n, collector);
        String selector = n.getSelector().toString();
        int line = n.getBegin().map(p -> p.line).orElse(-1);
        String method = findEnclosingMethodName(n);
        BranchCondition bc = new BranchCondition(BranchCondition.Kind.SWITCH, selector, line, method);
        n.getEntries().forEach(entry -> entry.getLabels().forEach(label -> bc.extra.add(label.toString())));
        collector.add(bc);
    }

    @Override
    public void visit(ForStmt n, List<ConditionCollector.BranchCondition> collector) {
        super.visit(n, collector);
        Optional<Expression> cmp = n.getCompare();
        String cond = cmp.map(Expression::toString).orElse("(no-compare)");
        int line = n.getBegin().map(p -> p.line).orElse(-1);
        String method = findEnclosingMethodName(n);
        BranchCondition bc = new BranchCondition(BranchCondition.Kind.FOR, cond, line, method);
        n.getInitialization().forEach(init -> bc.extra.add(init.toString()));
        n.getUpdate().forEach(upd -> bc.extra.add(upd.toString()));
        cmp.ifPresent(expression -> bc.atomicConstraints.addAll(decomposeExpression(expression)));
        collector.add(bc);
    }

    @Override
    public void visit(ForEachStmt n, List<ConditionCollector.BranchCondition> collector) {
        super.visit(n, collector);
        String iterable = n.getIterable().toString();
        int line = n.getBegin().map(p -> p.line).orElse(-1);
        String method = findEnclosingMethodName(n);
        BranchCondition bc = new BranchCondition(BranchCondition.Kind.FOREACH, iterable, line, method);
        bc.extra.add(n.getVariable().toString());
        collector.add(bc);
    }

    // Helper: find the enclosing method name (if any)
    private String findEnclosingMethodName(Node n) {
        Optional<MethodDeclaration> md = n.findAncestor(MethodDeclaration.class);
        return md.map(MethodDeclaration::getNameAsString).orElse("<init/initializer>");
    }

    // Decompose an expression into atomic constraints where possible.
    // This is conservative: it extracts comparisons and leaves method-calls/unknowns as-is.
    private List<AtomicConstraint> decomposeExpression(Expression expr) {
        List<AtomicConstraint> out = new ArrayList<>();
        if (expr.isBinaryExpr()) {
            BinaryExpr be = expr.asBinaryExpr();
            BinaryExpr.Operator op = be.getOperator();
            if (op == BinaryExpr.Operator.AND || op == BinaryExpr.Operator.OR) {
                // logical connectives: recurse on both sides
                out.addAll(decomposeExpression(be.getLeft()));
                out.addAll(decomposeExpression(be.getRight()));
            } else {
                // comparison or arithmetic: create atomic
                AtomicConstraint ac = new AtomicConstraint(be.getLeft().toString(), op.asString(), be.getRight().toString());
                out.add(ac);
            }
        } else if (expr.isMethodCallExpr()) {
            MethodCallExpr mc = expr.asMethodCallExpr();
            out.add(new AtomicConstraint(mc.toString(), "call", ""));
        } else if (expr.isUnaryExpr()) {
            UnaryExpr ue = expr.asUnaryExpr();
            out.add(new AtomicConstraint(ue.getExpression().toString(), ue.getOperator().asString(), ""));
        } else if (expr.isEnclosedExpr()) {
            out.addAll(decomposeExpression(expr.asEnclosedExpr().getInner()));
        } else if (expr.isInstanceOfExpr()) {
            InstanceOfExpr io = expr.asInstanceOfExpr();
            out.add(new AtomicConstraint(io.getExpression().toString(), "instanceof", io.getType().toString()));
        } else if (expr.isFieldAccessExpr()) {
            out.add(new AtomicConstraint(expr.toString(), "field", ""));
        } else if (expr.isNameExpr()) {
            out.add(new AtomicConstraint(expr.toString(), "name", ""));
        } else {
            // fallback: record the textual expression
            out.add(new AtomicConstraint(expr.toString(), "expr", ""));
        }
        return out;
    }

    // Simple POJO classes to hold constraints and branch metadata
    public static class AtomicConstraint {
        public String left;
        public String op;
        public String right;

        public AtomicConstraint(String left, String op, String right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", left, op, right).trim();
        }
    }

    public static class BranchCondition {
        public enum Kind { IF, SWITCH, FOR, FOREACH }

        public Kind kind;
        public String conditionText;
        public List<String> extra = new ArrayList<>();
        public int line;
        public String methodName;
        public List<AtomicConstraint> atomicConstraints = new ArrayList<>();

        public BranchCondition(Kind kind, String conditionText, int line, String methodName) {
            this.kind = kind;
            this.conditionText = conditionText;
            this.line = line;
            this.methodName = methodName;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(kind).append(" @").append(line).append(" in ").append(methodName).append(": ").append(conditionText);
            if (!extra.isEmpty()) sb.append(" extras=").append(extra);
            if (!atomicConstraints.isEmpty()) {
                sb.append("\n  Constraints:");
                for (AtomicConstraint ac : atomicConstraints) sb.append("\n    - ").append(ac.toString());
            }
            return sb.toString();
        }
    }
}
