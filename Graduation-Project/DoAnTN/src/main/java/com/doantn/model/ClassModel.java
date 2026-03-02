package com.doantn.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassModel: represents a Java class extracted from source code.
 * Contains metadata about the class and its methods.
 */
public class ClassModel {
    private String packageName;
    private String className;
    private String filePath;
    private List<MethodModel> methods;

    public ClassModel(String packageName, String className, String filePath) {
        this.packageName = packageName;
        this.className = className;
        this.filePath = filePath;
        this.methods = new ArrayList<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<MethodModel> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodModel> methods) {
        this.methods = methods;
    }

    public void addMethod(MethodModel method) {
        this.methods.add(method);
    }

    @Override
    public String toString() {
        return "ClassModel{" +
                "packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", filePath='" + filePath + '\'' +
                ", methods=" + methods.size() +
                '}';
    }
}

