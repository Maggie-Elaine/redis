package com.beneil.nettyrpc.serverStub;

import java.io.Serializable;

public class ClassInfo implements Serializable {
    private String className;   //类名
    private String methodName;  //方法名
    private Class<?>[] tpes;    //参数类型
    private Object[] objects;   //参数列表

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTpes() {
        return tpes;
    }

    public void setTpes(Class<?>[] tpes) {
        this.tpes = tpes;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }
}
