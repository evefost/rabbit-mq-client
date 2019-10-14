package com.eve.mq.client;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/13
 */
@Component
public class MqListener {

    @RabbitListener(queues = "xie-queue", containerFactory = "container_1")
    @RabbitHandler
    public void listenerShoppeMq(Message message){
        System.out.println("==============");
    }

    @RabbitListener(queues = "xie-queue2", containerFactory = "container_2")
    @RabbitHandler
    public void listenerShoppeMq2(Message message){
        System.out.println("========222222======");
    }
}
