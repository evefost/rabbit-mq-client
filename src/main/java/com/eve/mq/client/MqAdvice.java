package com.eve.mq.client;

import com.eve.common.ServerContextHolder;
import com.eve.mq.client.annotation.Tenant;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.cglib.proxy.Callback;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/15
 */
public class MqAdvice implements MethodInterceptor{
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        doBefore(invocation);
        try {
            return invocation.proceed();
        } finally {
            doAfter(invocation);
        }
    }

    private void doBefore(MethodInvocation invocation) throws IllegalAccessException {
        System.out.println("do before ...");
        Method method = invocation.getMethod();

        Tenant tenantA = AnnotationUtils.findAnnotation(method, Tenant.class);
        if (tenantA != null) {
            Object arg = invocation.getArguments()[0];
            Class<?> messageClass = arg.getClass();
            Field tenantField = fieldExist(messageClass, "tenantId");
            if(tenantField == null){
                System.out.printf("11111");
            }else {
                System.out.printf("22222");
            }
        }
    }

    private Field fieldExist(Class clzz, String fieldName) {
        Field field=null;
        try {
            field =  clzz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {

        }
        if(field == null){
            Class superclass = clzz.getSuperclass();
            if(superclass == null){
                return null;
            }
            try {
                field =  superclass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {

            }
        }
        return field;
    }

    private void doAfter(MethodInvocation invocation) {
        ServerContextHolder.setTenantId(null);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
