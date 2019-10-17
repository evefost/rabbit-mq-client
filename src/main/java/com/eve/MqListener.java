package com.eve;

import com.eve.common.ServerContextHolder;
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
    public void listenerShoppeMq(User2 message) {
        System.out.println("业务端处理tenantId:" + ServerContextHolder.getTenantId());
    }

    //    @RabbitListener(queues = "xie-queue2", containerFactory = "container_2")
//    @RabbitHandler
//    public void listenerShoppeMq2(Message message) {
//        System.out.println("========222222======");
//    }

    public void listenerShoppeMq2(User message) {
        System.out.println("========222222======");
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
