package com.eve.mq.client;

import com.eve.mq.client.annotation.Producer;
import com.eve.mq.client.annotation.Routekey;
import com.eve.mq.client.support.MethodInfo;
import com.eve.mq.client.support.ProducerFactoryBean;
import com.eve.mq.client.support.TopicPointInfo;
import com.eve.spring.ClassScaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
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
public class ProducerApiRegistryPostProcessor implements ResourceLoaderAware, BeanDefinitionRegistryPostProcessor {

    protected final Logger logger = LoggerFactory.getLogger(ProducerApiRegistryPostProcessor.class);

    private BeanDefinitionRegistry registry;

    protected ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private void scanProducers(Set<String> basePackages, TopicPointInfo producerInfo,
                               BeanDefinitionRegistry registry) throws ClassNotFoundException {
        ClassScaner classScaner = new ClassScaner();
        classScaner.setResourceLoader(resourceLoader);
        List<Class<?>> classes = classScaner.doScan("com.eve");
        for (Class clazz : classes) {
            registerProducerApi(clazz, producerInfo);
        }

    }

    protected void registerProducerApi(Class targetClass, TopicPointInfo producerInfo) throws ClassNotFoundException {
        String beanName = "$" + targetClass.getSimpleName();
        String beanClassName = targetClass.getName();
        Producer annotation = AnnotationUtils.findAnnotation(targetClass, Producer.class);
        if (annotation == null) {
            return;
        }
        logger.debug("即将创建的实例名:" + beanName);
        producerInfo.setTargetClass(targetClass);
        producerInfo.setContainerName(annotation.containerFactory());
        parseVirtualInfo(producerInfo);
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(ProducerFactoryBean.class);
        definition.addPropertyValue("type", beanClassName);
        definition.addPropertyValue("containerName", annotation.containerFactory());
        definition.addPropertyValue("producerInfo", producerInfo);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setPrimary(false);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName,
                new String[]{});
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private void parseVirtualInfo(TopicPointInfo pointInfo) throws ClassNotFoundException {
        Class<?> targetClass = pointInfo.getTargetClass();
        Method[] methods;
        if (targetClass.isInterface()) {
            methods = targetClass.getMethods();
        } else {
            methods = targetClass.getDeclaredMethods();
        }
        for (Method method : methods) {
            method.setAccessible(true);
            Routekey routeKeyAnno = method.getAnnotation(Routekey.class);
            MethodInfo methodInfo = new MethodInfo();
            pointInfo.putMethodInfo(method, methodInfo);
            String methodTopic = routeKeyAnno.value();
            String exchange = routeKeyAnno.exchange();
            methodInfo.setContainerName(pointInfo.getContainerName());
            methodInfo.setTargetClass(targetClass);
            methodInfo.setMethod(method);
            methodInfo.setRouteKey(methodTopic);
            methodInfo.setExchange(exchange);
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
        TopicPointInfo producerInfo = new TopicPointInfo();
        Set<String> basePackages = new HashSet<>();
        basePackages.add("com.eve");
        try {
            scanProducers(basePackages, producerInfo, registry);
        } catch (ClassNotFoundException e) {
            logger.error("process registry producer api error ", e);
        }
    }


}
