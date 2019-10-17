package com.eve.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 类说明
 * <p>
 *
 * @author xieyang
 * @version 1.0.0
 * @date 2019/10/14
 */
public class PropertiesUtils {


    public static <P, A extends Annotation> List<PropertiesInfo<P, A>> scanProperties(ConfigurableListableBeanFactory beanFactory, Class<P> t, Class<A> an) throws ClassNotFoundException {
        List<PropertiesInfo<P, A>> list = new ArrayList<>();
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName == null) {
                continue;
            }
            Class<?> targetClass = classLoader.loadClass(beanClassName);
            Method[] declaredMethods = targetClass.getDeclaredMethods();
            for (Method m : declaredMethods) {
                A dbAnnotation = AnnotationUtils.findAnnotation(m, an);
                if (dbAnnotation != null) {
                    String prpBeanName = m.getName();
                    P properties = (P) beanFactory.getBean(prpBeanName);
                    PropertiesInfo<P, A> propertiesInfo = new PropertiesInfo();
                    propertiesInfo.setMethod(m);
                    propertiesInfo.setProperties(properties);
                    propertiesInfo.setTargetClass(targetClass);
                    propertiesInfo.setAnnotation(dbAnnotation);
                    propertiesInfo.setBeanName(prpBeanName);
                    list.add(propertiesInfo);
                }
            }
        }
        return list;
    }


    public static class PropertiesInfo<P, A> {

        private Class targetClass;

        private Method method;

        private A annotation;

        private P properties;

        private String beanName;


        public Class getTargetClass() {
            return targetClass;
        }

        public void setTargetClass(Class targetClass) {
            this.targetClass = targetClass;
        }

        public A getAnnotation() {
            return annotation;
        }

        public void setAnnotation(A annotation) {
            this.annotation = annotation;
        }

        public String getBeanName() {
            return beanName;
        }

        public void setBeanName(String beanName) {
            this.beanName = beanName;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public P getProperties() {
            return properties;
        }

        public void setProperties(P properties) {
            this.properties = properties;
        }
    }

}
