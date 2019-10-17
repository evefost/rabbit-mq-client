package com.eve.mq.client.rabbit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Proxy;

/**
 * 生产bean处理
 */
class ProducerFactoryBean implements FactoryBean<Object>, InitializingBean, ApplicationContextAware, EnvironmentAware {

    private ApplicationContext applicationContext;

    protected Environment environment;

    private Class<?> type;

    private String containerName;

    private ProducerInfo producerInfo;


    @Override
    public Object getObject() throws Exception {
        Object proxy = Proxy.newProxyInstance(ProducerFactoryBean.class.getClassLoader(), new Class[]{type}, new ProducerInvocationHandler(producerInfo, applicationContext));
        return proxy;
    }


    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public ProducerInfo getProducerInfo() {
        return producerInfo;
    }

    public void setProducerInfo(ProducerInfo producerInfo) {
        this.producerInfo = producerInfo;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public String toString() {
        return new StringBuilder("MyTestFactoryBean{")
                .append("type=").append(type).append(", ")
                .append("}").toString();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
