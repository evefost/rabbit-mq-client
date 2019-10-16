package com.eve.mq.client;

import com.eve.common.ClassUtils;
import com.eve.common.ServerContextHolder;
import com.eve.mq.client.annotation.Tenant;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RabbitMqListerTenantAdvice implements MethodInterceptor {

    protected final Logger logger = LoggerFactory.getLogger(RabbitMqListerTenantAdvice.class);

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
        Method method = invocation.getMethod();
        Tenant tenantA = AnnotationUtils.findAnnotation(method, Tenant.class);
        if (tenantA != null) {
            Object arg = invocation.getArguments()[0];
            Class<?> messageClass = arg.getClass();
            Field tenantField = ClassUtils.fieldExist(messageClass, "tenantId");
            tenantField.setAccessible(true);
            String tenantId = (String) tenantField.get(arg);
            if (tenantId == null) {
                logger.warn(method.getName() + " mq listener mark Tenant Annotation but tenantId not found in java bean");
            }else {
                ServerContextHolder.setTenantId((String) tenantField.get(arg));
            }

        }
    }


    private void doAfter(MethodInvocation invocation) {
        ServerContextHolder.setTenantId(null);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
