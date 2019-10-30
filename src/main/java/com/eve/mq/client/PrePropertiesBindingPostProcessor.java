/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eve.mq.client;

import com.eve.common.annotation.AsProperties;
import com.eve.spring.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.PropertySources;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 提前注入配置属性到bean里
 * {@link BeanPostProcessor} to bind {@link PropertySources} to beans annotated with
 * {@link ConfigurationProperties}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @author Christian Dupuis
 * @author Stephane Nicoll
 */
@Component
public class PrePropertiesBindingPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    protected final Logger logger = LoggerFactory.getLogger(PrePropertiesBindingPostProcessor.class);


    /**
     * Return the order of the bean.
     *
     * @return the order
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 9;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        PropertySourcesPlaceholderConfigurer placeholderConfigurer = beanFactory.getBean(PropertySourcesPlaceholderConfigurer.class);
        placeholderConfigurer.postProcessBeanFactory(beanFactory);
        List<PropertiesUtils.PropertiesInfo<Object, AsProperties>> propertiesInfos = null;
        try {
            propertiesInfos = PropertiesUtils.scanProperties(beanFactory, Object.class, AsProperties.class);
        } catch (Exception e) {
            logger.error("pre binding properties error :", e);
        }
        //注入属性值
        ConfigurationPropertiesBindingPostProcessor bindingPostProcessor = beanFactory.getBean(ConfigurationPropertiesBindingPostProcessor.class);

        for (PropertiesUtils.PropertiesInfo<Object, AsProperties> p : propertiesInfos) {
            Object bean = p.getProperties();
            String beanName = p.getBeanName();
            Method method = p.getMethod();
            ConfigurationProperties annotation = AnnotationUtils.findAnnotation(method, ConfigurationProperties.class);
            if (annotation != null) {
                bindingPostProcessor.postProcessBeforeInitialization(bean, beanName);
            }
        }

    }


}
