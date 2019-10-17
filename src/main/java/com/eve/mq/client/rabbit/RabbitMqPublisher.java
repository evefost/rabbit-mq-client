package com.eve.mq.client.rabbit;

import com.eve.common.ServerContextHolder;
import com.eve.mq.client.MessagePublisher;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/17
 */
@Component
public class RabbitMqPublisher<T> implements MessagePublisher<RabbitMessage<T>>, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void publish(RabbitMessage<T> message) {
        checkConfig(message);
        doPublish(message);
    }

    private void checkConfig(RabbitMessage<T> message) {
        if (StringUtils.isEmpty(message.getContainerFactory())) {
            throw new RuntimeException("请配置containerFactory");
        }
        if (StringUtils.isEmpty(message.getExchange())) {
            throw new RuntimeException("请配置exchange");
        }
        if (StringUtils.isEmpty(message.getRouteKey())) {
            throw new RuntimeException("请配置routeKey");
        }

    }


    protected void doPublish(RabbitMessage<T> sourceMsg) {
        String templateName = sourceMsg.getContainerFactory() + "_template";
        RabbitTemplate template = applicationContext.getBean(templateName, RabbitTemplate.class);
        String exchange = sourceMsg.getExchange();
        String routeKey = sourceMsg.getRouteKey();
        MessageProperties messageProperties = new MessageProperties();
        String tenantId = ServerContextHolder.getTenantId();
        if (!StringUtils.isEmpty(tenantId)) {
            messageProperties.setHeader("tenantId", tenantId);
        }
        Message message = messageConverter.toMessage(sourceMsg.getData(), messageProperties);
        template.convertAndSend(exchange, routeKey, message);
    }
}
