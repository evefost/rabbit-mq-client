package com.eve.mq.client;

import com.eve.mq.client.annotation.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.*;

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
        if (isListener(bean.getClass())) {
            System.out.println("xxxxxx" + beanName);
          return   createProxy(bean);
        }
        return bean;
    }


    private Object createProxy(Object delegate) {

        ProxyFactory factory = new ProxyFactory();
        MqAdvice mqAdvice = new MqAdvice();
        factory.addAdvisor(new DefaultPointcutAdvisor(Pointcut.TRUE, mqAdvice));

        factory.setProxyTargetClass(true);
//        factory.addInterface(SimpleMessageListenerContainer.ContainerDelegate.class);
        factory.setTarget(delegate);
        Object proxy =  factory.getProxy(delegate.getClass().getClassLoader());
        return proxy;
    }


    private boolean isListener(Class<?> targetClass) {
        Collection<Tenant> classLevelListeners = findListenerAnnotations(targetClass);
        final boolean hasClassLevelListeners = classLevelListeners.size() > 0;
        final List<Method> multiMethods = new ArrayList<Method>();
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {

            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                Collection<Tenant> listenerAnnotations = findListenerAnnotations(method);
                classLevelListeners.addAll(listenerAnnotations);

            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return classLevelListeners.size() > 0;
    }

    private Collection<Tenant> findListenerAnnotations(Class<?> clazz) {
        Set<Tenant> listeners = new HashSet<Tenant>();
        Tenant ann = AnnotationUtils.findAnnotation(clazz, Tenant.class);
        if (ann != null) {
            listeners.add(ann);
        }
        return listeners;
    }

    private Collection<Tenant> findListenerAnnotations(Method method) {
        Set<Tenant> listeners = new HashSet<Tenant>();
        Tenant ann = AnnotationUtils.findAnnotation(method, Tenant.class);
        if (ann != null) {
            listeners.add(ann);
        }
        return listeners;
    }
}
