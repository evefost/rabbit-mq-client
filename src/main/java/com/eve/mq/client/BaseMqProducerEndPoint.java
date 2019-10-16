package com.eve.mq.client;

import java.lang.reflect.Method;

/**
 * @author xieyang
 * @date 18/7/14
 */
public class BaseMqProducerEndPoint {

    private Class targetClass;

    private Object target;

    private Method method;

    private boolean isTenant;

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isTenant() {
        return isTenant;
    }

    public void setTenant(boolean tenant) {
        isTenant = tenant;
    }
}
