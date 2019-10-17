package com.eve.mq.client.rabbit;

import com.eve.common.ServerContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

/**
 * 拦截租户信息(多租户)
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/15
 */
@Component
public class RabbitMqListerTenantAdvice implements RabbitMqListerAdvice {

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

    private void doBefore(MethodInvocation invocation) {
        Message message = (Message) invocation.getArguments()[1];
        MessageProperties messageProperties = message.getMessageProperties();
        String consumerQueue = messageProperties.getConsumerQueue();
        String tenantId = (String) messageProperties.getHeaders().get("tenantId");
        logger.info("收到消息queue[{}] tenantId[{}]", consumerQueue, tenantId);
        ServerContextHolder.setTenantId(tenantId);
    }

    private void doAfter(MethodInvocation invocation) {
        ServerContextHolder.setTenantId(null);
    }


}
