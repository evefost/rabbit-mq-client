package com.eve.mq.client;

import com.eve.mq.client.rabbit.RabbitMqListerAdvice;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/15
 */

@Order(HIGHEST_PRECEDENCE + 1)
public class RabbitMqRetryAdvice implements RabbitMqListerAdvice {

    protected final Logger logger = LoggerFactory.getLogger(RabbitMqRetryAdvice.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        int retryCount = 1;
        while (retryCount > 0) {
            try {
                return invocation.proceed();
            } catch (Throwable ex) {
                retryCount--;
                if (retryCount < 1) {
                    logger.error("message process error ", ex);
                }
            }
        }
        return null;
    }


}
