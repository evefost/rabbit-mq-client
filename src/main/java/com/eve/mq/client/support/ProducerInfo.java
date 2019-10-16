package com.eve.mq.client.support;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xieyang on 18/7/15.
 */
public class ProducerInfo {


    private String envPrefix;

    private String containerName;

    private Class targetClass;


    private Map<String/*evn+topic:tag*/, RabbitMqProducerEndPoint> topicMethodMappings = new HashMap<String, RabbitMqProducerEndPoint>();

    private Map<Method, RabbitMqProducerEndPoint> methodInfoMappings = new HashMap<Method, RabbitMqProducerEndPoint>();

    private Map<String/*topic*/, List<String>/*tag*/> topicTags = new HashMap<>();

    public Map<String, RabbitMqProducerEndPoint> getTopicMethodMappings() {
        return topicMethodMappings;
    }

    public Map<Method, RabbitMqProducerEndPoint> getMethodInfoMappings() {
        return methodInfoMappings;
    }

    public RabbitMqProducerEndPoint getMethodInfo(String key) {
        return topicMethodMappings.get(key);
    }

    public RabbitMqProducerEndPoint putMethodInfo(String key, RabbitMqProducerEndPoint methodInfo) {
        return topicMethodMappings.put(key, methodInfo);
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

    public Map<String, List<String>> getTopicTags() {
        return topicTags;
    }

    public void setTopicTags(Map<String, List<String>> topicTags) {
        this.topicTags = topicTags;
    }

    public List<String> getTags(String topic) {
        return this.topicTags.get(topic);
    }

    public void setTopicMethodMappings(
            Map<String, RabbitMqProducerEndPoint> topicMethodMappings) {
        this.topicMethodMappings = topicMethodMappings;
    }

    public void setMethodInfoMappings(
            Map<Method, RabbitMqProducerEndPoint> methodInfoMappings) {
        this.methodInfoMappings = methodInfoMappings;
    }
}
