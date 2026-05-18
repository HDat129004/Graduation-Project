package com.doantn.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MethodModel: represents a Java method extracted from source code.
 * Contains method metadata including name, return type, and parameters.
 */
public class MethodModel {
    private String methodName;
    private String returnType;
    private List<ParameterModel> parameters;
    private List<com.doantn.model.BranchCondition> branchConditions;
    private int lineNumber;
    private boolean isPublic;
    private boolean isStatic;

    public MethodModel(String methodName, String returnType) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
        this.branchConditions = new ArrayList<>();
        this.isPublic = false;
        this.isStatic = false;
        this.lineNumber = -1;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<ParameterModel> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterModel> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(ParameterModel param) {
        this.parameters.add(param);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public List<com.doantn.model.BranchCondition> getBranchConditions() {
        return branchConditions;
    }

    public void setBranchConditions(List<com.doantn.model.BranchCondition> branchConditions) {
        this.branchConditions = branchConditions;
    }

    public void addBranchCondition(com.doantn.model.BranchCondition bc) {
        this.branchConditions.add(bc);
    }

    @Override
    public String toString() {
        return "MethodModel{" +
                "methodName='" + methodName + '\'' +
                ", returnType='" + returnType + '\'' +
                ", parameters=" + parameters.size() +
                ", isPublic=" + isPublic +
                ", isStatic=" + isStatic +
                '}';
    }
}

