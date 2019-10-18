package com.eve.mq.client;

import com.eve.common.ServerContextHolder;
import com.eve.mq.client.rabbit.RabbitMqListerAdvice;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 拦截租户信息(多租户)
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/15
 */
@Order(HIGHEST_PRECEDENCE)
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
        String tenantId = (String) messageProperties.getHeaders().get("tenant-id");
        ServerContextHolder.setTenantId(tenantId);
        if (logger.isDebugEnabled()) {
            String consumerQueue = messageProperties.getConsumerQueue();
            logger.debug("收到消息queue[{}] tenantId[{}]", consumerQueue, tenantId);
        }
    }

    private void doAfter(MethodInvocation invocation) {
        ServerContextHolder.setTenantId(null);
    }


}
