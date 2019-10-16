package com.eve.mq.client;

import com.eve.mq.client.annotation.Producer;
import com.eve.mq.client.annotation.Routekey;
import com.eve.mq.client.annotation.Tenant;
import com.eve.mq.client.support.ProducerFactoryBean;
import com.eve.mq.client.support.ProducerInfo;
import com.eve.mq.client.support.RabbitMqProducerEndPoint;
import com.eve.spring.ClassScaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

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
public class RabbitMqProducerApiRegistryPostProcessor implements ResourceLoaderAware, BeanDefinitionRegistryPostProcessor {

    protected final Logger logger = LoggerFactory.getLogger(RabbitMqProducerApiRegistryPostProcessor.class);

    private BeanDefinitionRegistry registry;

    protected ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private void scanProducers(Set<String> basePackages, ProducerInfo producerInfo) throws ClassNotFoundException {
        ClassScaner classScaner = new ClassScaner();
        classScaner.setResourceLoader(resourceLoader);
        Set<Class> clazzes = new HashSet<>();
        for (String page : basePackages) {
            List<Class<?>> classes = classScaner.doScan(page);
            for (Class clz : classes) {
                clazzes.add(clz);
            }
        }
        for (Class clazz : clazzes) {
            registerProducerApi(clazz, producerInfo);
        }
    }

    protected void registerProducerApi(Class targetClass, ProducerInfo producerInfo) throws ClassNotFoundException {
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

    private void parseVirtualInfo(ProducerInfo pointInfo) throws ClassNotFoundException {
        Class<?> targetClass = pointInfo.getTargetClass();
        Method[] methods;
        if (targetClass.isInterface()) {
            methods = targetClass.getMethods();
        } else {
            methods = targetClass.getDeclaredMethods();
        }
        Producer annotation = AnnotationUtils.findAnnotation(targetClass, Producer.class);
        String classExchange = annotation.exchange();
        Tenant classTenantA = AnnotationUtils.findAnnotation(targetClass, Tenant.class);
        for (Method method : methods) {
            method.setAccessible(true);
            Routekey routeKeyA = AnnotationUtils.findAnnotation(method, Routekey.class);
            Tenant mTenantA = AnnotationUtils.findAnnotation(method, Tenant.class);
            RabbitMqProducerEndPoint methodInfo = new RabbitMqProducerEndPoint();
            pointInfo.putMethodInfo(method, methodInfo);
            String methodTopic = routeKeyA.value();
            String exchange = StringUtils.isEmpty(classExchange) ? routeKeyA.exchange() : classExchange;
            if (StringUtils.isEmpty(exchange)) {
                throw new RuntimeException("请配置exchange");
            }
            methodInfo.setContainerName(pointInfo.getContainerName());
            methodInfo.setTargetClass(targetClass);
            methodInfo.setMethod(method);
            methodInfo.setRouteKey(methodTopic);
            methodInfo.setExchange(exchange);
            methodInfo.setTenant(classTenantA != null || mTenantA != null);
        }
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
        ProducerInfo producerInfo = new ProducerInfo();
        Set<String> basePackages = getComponentScanningPackages(registry);
        try {
            scanProducers(basePackages, producerInfo);
        } catch (ClassNotFoundException e) {
            logger.error("process registry producer api error ", e);
        }
    }


    protected Set<String> getComponentScanningPackages(
            BeanDefinitionRegistry registry) {
        Set<String> packages = new LinkedHashSet<String>();
        String[] names = registry.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition definition = registry.getBeanDefinition(name);
            if (definition instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition annotatedDefinition = (AnnotatedBeanDefinition) definition;
                addComponentScanningPackages(packages,
                        annotatedDefinition.getMetadata());
            }
        }
        return packages;
    }

    private void addPackages(Set<String> packages, String[] values) {
        if (values != null) {
            Collections.addAll(packages, values);
        }
    }

    private void addClasses(Set<String> packages, String[] values) {
        if (values != null) {
            for (String value : values) {
                packages.add(ClassUtils.getPackageName(value));
            }
        }
    }

    private void addComponentScanningPackages(Set<String> packages,
                                              AnnotationMetadata metadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
                .getAnnotationAttributes(ComponentScan.class.getName(), true));
        if (attributes != null) {
            addPackages(packages, attributes.getStringArray("value"));
            addPackages(packages, attributes.getStringArray("basePackages"));
            addClasses(packages, attributes.getStringArray("basePackageClasses"));
            if (packages.isEmpty()) {
                packages.add(ClassUtils.getPackageName(metadata.getClassName()));
            }
        }
    }
}
