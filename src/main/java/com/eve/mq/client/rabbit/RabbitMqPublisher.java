package com.eve.mq.client.rabbit;

import com.eve.common.GlobalConstant;
import com.eve.common.ServerContextHolder;
import com.eve.mq.client.MessagePublisher;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/17
 */

class RabbitMqPublisher<T> implements MessagePublisher<RabbitMessage<T>> {

    private ApplicationContext applicationContext;

    private AbstractMessageConverter messageConverter;


    public RabbitMqPublisher(ApplicationContext applicationContext, AbstractMessageConverter messageConverter) {
        this.applicationContext = applicationContext;
        this.messageConverter = messageConverter;
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
            messageProperties.setHeader(GlobalConstant.TENANT_ID, tenantId);
        }
        String uuid = (String) ServerContextHolder.getData(GlobalConstant.LOG_UUID);
        if (!StringUtils.isEmpty(uuid)) {
            messageProperties.setHeader(GlobalConstant.LOG_UUID, uuid);
        }
        String token = ServerContextHolder.getToken();
        if (!StringUtils.isEmpty(token)) {
            messageProperties.setHeader(GlobalConstant.TOKEN, token);
        }

        Message message = messageConverter.toMessage(sourceMsg.getData(), messageProperties);
        template.convertAndSend(exchange, routeKey, message);
    }
}
