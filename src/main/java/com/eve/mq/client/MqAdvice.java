package com.eve.mq.client;

import com.eve.common.ServerContextHolder;
import com.eve.mq.client.annotation.Tenant;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
public class MqAdvice implements MethodInterceptor {
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
        Message msg = (Message) invocation.getArguments()[1];
        MessageProperties messageProperties1 = msg.getMessageProperties();
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        Tenant tenantA = AnnotationUtils.findAnnotation(method, Tenant.class);
        if (tenantA != null) {
            Object arg = invocation.getArguments()[0];
            Class<?> messageClass = arg.getClass();
            Message message = (Message) arg;
            MessageProperties messageProperties = message.getMessageProperties();
            Object o = messageConverter.fromMessage(message);
            Field tenantField = fieldExist(messageClass, "tenant");
//            tenantField.get(message);
//            if (argument instanceof Message) {
//                WrapperData data = (WrapperData) argument;
//                ServerContextHolder.setTenantId(data.getTenantId());
//            } else {
//                throw new RuntimeException("租户信息不存在");
//            }
        }
    }

    private Field fieldExist(Class clzz, String fieldName) {
        try {
            return clzz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void doAfter(MethodInvocation invocation) {
        ServerContextHolder.setTenantId(null);
    }
}
