package com.eve.server.context;

import com.eve.common.GlobalConstant;
import com.eve.common.ServerContextHolder;
import com.eve.mq.client.rabbit.RabbitMqListerAdvice;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * 拦截基本context
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/15
 */
@Order(HIGHEST_PRECEDENCE+1)
@Component
public class RabbitMqListerContextAdvice implements RabbitMqListerAdvice {

    protected final Logger logger = LoggerFactory.getLogger(RabbitMqListerContextAdvice.class);

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
        Map<String, Object> headers = messageProperties.getHeaders();
        String tenantId = (String) headers.get(GlobalConstant.TENANT_ID);
        String uuid = (String) headers.get(GlobalConstant.LOG_UUID);
        String token = (String) headers.get(GlobalConstant.TOKEN);
        ServerContextHolder.setTenantId(tenantId);
        ServerContextHolder.setData(GlobalConstant.LOG_UUID, uuid);
        ServerContextHolder.setToken(token);
        if (logger.isDebugEnabled()) {
            String consumerQueue = messageProperties.getConsumerQueue();
            logger.debug("收到消息queue[{}] tenantId[{}]", consumerQueue, tenantId);
        }
    }

    private void doAfter(MethodInvocation invocation) {
        ServerContextHolder.setTenantId(null);
        ServerContextHolder.setData(GlobalConstant.LOG_UUID, null);
        ServerContextHolder.setToken(null);
    }


}
