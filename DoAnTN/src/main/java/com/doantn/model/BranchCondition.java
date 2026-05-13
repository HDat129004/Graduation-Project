package com.doantn.model;

/**
 * BranchCondition: Đại diện cho một điều kiện rẽ nhánh (if/else) được tìm thấy trong thân hàm.
 * Phục vụ cho kỹ thuật Constraint Solving để sinh dữ liệu test.
 */
public class BranchCondition {
    private String paramName;
    private String operator;
    private String boundaryValue;
    private String exceptionType; // Nếu nhánh này ném ngoại lệ (throw)

    public BranchCondition() {}

    public BranchCondition(String paramName, String operator, String boundaryValue) {
        this.paramName = paramName;
        this.operator = operator;
        this.boundaryValue = boundaryValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getBoundaryValue() {
        return boundaryValue;
    }

    public void setBoundaryValue(String boundaryValue) {
        this.boundaryValue = boundaryValue;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
}