package com.eve;

import com.eve.mq.client.annotation.Consumer;
import com.eve.mq.client.annotation.Tenant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/13
 */
@Consumer
public class MqListener2 {


    @Tenant
    @RabbitListener(queues = "xie-queue2", containerFactory = "container_1")
    @RabbitHandler
    public void listenerShoppeMq(User message) {
        System.out.println("==============" + message.getName());
    }

//    @RabbitListener(queues = "xie-queue2", containerFactory = "container_2")
//    @RabbitHandler
//    public void listenerShoppeMq2(Message message) {
//        System.out.println("========222222======");
//    }

    public void listenerShoppeMq2(User message) {
        System.out.println("========222222======");
    }


}
