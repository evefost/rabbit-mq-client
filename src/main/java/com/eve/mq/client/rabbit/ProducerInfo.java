package com.eve.mq.client.rabbit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xieyang on 18/7/15.
 */
public class ProducerInfo {


    private String envPrefix;

    private String containerName;

    private Class targetClass;

    private Map<Method, RabbitMqProducerEndPoint> methodInfoMappings = new HashMap<Method, RabbitMqProducerEndPoint>();


    public Map<Method, RabbitMqProducerEndPoint> getMethodInfoMappings() {
        return methodInfoMappings;
    }


    public String getEnvPrefix() {
        return envPrefix;
    }

    public void setEnvPrefix(String envPrefix) {
        this.envPrefix = envPrefix;
    }


    public RabbitMqProducerEndPoint getMethodInfo(Method method) {
        return methodInfoMappings.get(method);
    }

    public RabbitMqProducerEndPoint putMethodInfo(Method method, RabbitMqProducerEndPoint methodInfo) {
        return methodInfoMappings.put(method, methodInfo);
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }


    public void setMethodInfoMappings(
            Map<Method, RabbitMqProducerEndPoint> methodInfoMappings) {
        this.methodInfoMappings = methodInfoMappings;
    }
}
