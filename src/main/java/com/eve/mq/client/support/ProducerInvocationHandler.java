package com.eve.mq.client.support;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 生产者消息处理
 *
 * @author xieyang
 * @date 18/7/14
 */
public class ProducerInvocationHandler implements InvocationHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    private TopicPointInfo producerInfo;

    private ApplicationContext applicationContext;

    private String envPrefix;

    public ProducerInvocationHandler(TopicPointInfo producerInfo, ApplicationContext applicationContext) {
        this.producerInfo = producerInfo;
        this.applicationContext = applicationContext;
        this.envPrefix = producerInfo.getEnvPrefix();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if ("equals".equals(method.getName())) {
            try {
                Object
                        otherHandler =
                        args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return method.hashCode();
        } else if ("toString".equals(method.getName())) {
            return method.toString();
        }

        MethodInfo methodInfo = producerInfo.getMethodInfo(method);
        String exchange = methodInfo.getExchange();
        String key = methodInfo.getRouteKey();

        logger.info("发布消息exchange[{}]routeKey:[{}{}], 方法参数: {}", exchange, key, args);
        if (args != null && args.length == 1) {
            RabbitTemplate template = applicationContext.getBean(methodInfo.getContainerName() + "_template", RabbitTemplate.class);
            template.convertAndSend(methodInfo.getExchange(), key, args[0]);
        } else {
            throw new RuntimeException("parameters are not support");
        }
        Class<?> returnType = method.getReturnType();
        if (void.class == returnType) {
            return null;
        }
        return null;

    }
}
