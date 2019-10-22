package com.eve.mq.client;

import com.alibaba.fastjson.JSON;
import com.eve.mq.client.rabbit.RabbitMqListerAdvice;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 *
 * @author xieyang
 * @version 1.0.0
 * @date 2019/10/15
 */

@Order(HIGHEST_PRECEDENCE)
public class RabbitMqRetryAdvice implements RabbitMqListerAdvice {

    protected final Logger logger = LoggerFactory.getLogger(RabbitMqRetryAdvice.class);

    private AbstractMessageConverter messageConverter;

    public RabbitMqRetryAdvice(AbstractMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        int retryCount = 1;
        while (retryCount > 0) {
            try {
                return invocation.proceed();
            } catch (Throwable ex) {
                retryCount--;
                if (retryCount < 1) {
                    Message message = (Message) invocation.getArguments()[1];
                    MessageProperties properties = message.getMessageProperties();
                    Object params = messageConverter.fromMessage(message);
                    logger.error("message process error params [{}] properties [{}]", JSON.toJSONString(params), JSON.toJSONString(properties), ex);
                }
            }
        }
        return null;
    }


}
