package com.eve.mq.client.rabbit;


import com.eve.mq.client.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private ProducerInfo producerInfo;

    private String envPrefix;

    private MessagePublisher<RabbitMessage> publisher;

    public ProducerInvocationHandler(ProducerInfo producerInfo, MessagePublisher<RabbitMessage> publisher) {
        this.producerInfo = producerInfo;
        this.publisher = publisher;
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
        if (args != null && args.length == 1) {
            Object targetMsg = args[0];
            RabbitMqProducerEndPoint methodInfo = producerInfo.getMethodInfo(method);
            RabbitMessage message = new RabbitMessage();
            message.setExchange(methodInfo.getExchange());
            message.setRouteKey(methodInfo.getRouteKey());
            message.setContainerFactory(methodInfo.getContainerName());
            message.setData(targetMsg);
            publisher.publish(message);
        } else {
            throw new RuntimeException("multiple parameters are not support");
        }
        Class<?> returnType = method.getReturnType();
        if (void.class == returnType) {
            return null;
        }
        return null;

    }

}
