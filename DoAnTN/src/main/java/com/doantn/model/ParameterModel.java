package com.doantn.model;

/**
 * ParameterModel: represents a method parameter.
 */
public class ParameterModel {
    private String paramName;
    private String paramType;

    public ParameterModel(String paramName, String paramType) {
        this.paramName = paramName;
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    @Override
    public String toString() {
        return paramName + ": " + paramType;
    }
}

