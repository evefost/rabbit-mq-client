package com.eve.mq.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/14
 */
@Component
public class ConsumerRegistryPostProcessor implements ResourceLoaderAware, BeanPostProcessor, Ordered {

    protected final Logger logger = LoggerFactory.getLogger(ConsumerRegistryPostProcessor.class);


    protected ResourceLoader resourceLoader;

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 1;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isRabbitMqListener(bean.getClass())) {
            return createProxy(bean);
        }
        return bean;
    }


    private Object createProxy(Object delegate) {
        ProxyFactory factory = new ProxyFactory();
        RabbitMqListerTenantAdvice mqAdvice = new RabbitMqListerTenantAdvice();
        factory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, mqAdvice));
        factory.setProxyTargetClass(true);
        factory.setTarget(delegate);
        Object proxy = factory.getProxy(delegate.getClass().getClassLoader());
        return proxy;
    }


    private boolean isRabbitMqListener(Class<?> targetClass) {
        Collection<RabbitListener> classLevelListeners = findListenerAnnotations(targetClass);
        final boolean hasClassLevelListeners = classLevelListeners.size() > 0;
        if (hasClassLevelListeners) {
            return true;
        }
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {

            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                Collection<RabbitListener> listenerAnnotations = findListenerAnnotations(method);
                classLevelListeners.addAll(listenerAnnotations);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);

        return classLevelListeners.size() > 0;
    }


    private Collection<RabbitListener> findListenerAnnotations(Class<?> clazz) {
        Set<RabbitListener> listeners = new HashSet<RabbitListener>();
        RabbitListener ann = AnnotationUtils.findAnnotation(clazz, RabbitListener.class);
        if (ann != null) {
            listeners.add(ann);
        }
        RabbitListeners anns = AnnotationUtils.findAnnotation(clazz, RabbitListeners.class);
        if (anns != null) {
            Collections.addAll(listeners, anns.value());
        }
        return listeners;
    }


    private Collection<RabbitListener> findListenerAnnotations(Method method) {
        Set<RabbitListener> listeners = new HashSet<RabbitListener>();
        RabbitListener ann = AnnotationUtils.findAnnotation(method, RabbitListener.class);
        if (ann != null) {
            listeners.add(ann);
        }
        RabbitListeners anns = AnnotationUtils.findAnnotation(method, RabbitListeners.class);
        if (anns != null) {
            Collections.addAll(listeners, anns.value());
        }
        return listeners;
    }
}
