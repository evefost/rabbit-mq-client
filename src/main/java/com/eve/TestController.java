package com.eve;

import com.eve.mq.client.MessagePublisher;
import com.eve.mq.client.rabbit.RabbitMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/13
 */
@RestController
public class TestController {

//    @Resource(name = "aaa")
//    RabbitTemplate template;


    @Autowired
    MessagePublisher messagePublisher;

    @Autowired
    private TestProducerApi producerApi;

    @GetMapping("/publish")
    String publish(){
        User user = new User();
        user.setAge(111);
        user.setName("xieayng");
        producerApi.sendUser(user);
        return "success";
    }

    @GetMapping("/publish2")
    String publish2(){
        User user = new User();
        user.setAge(111);
        user.setName("xieayng");
        RabbitMessage<User> message = new RabbitMessage<>();
//        message.setExchange("xie_test");
        message.setRouteKey("xie-rout-key");
        message.setContainerFactory("container_2");
        message.setData(user);
        messagePublisher.publish(message);
        return "success";
    }

    @Autowired
    private MqListener mqListener;

    @GetMapping("/publish3")
    String publish3() {
        User user = new User();
        user.setAge(111);
        user.setName("xieayng");
        mqListener.listenerShoppeMq2(user);
        return "success";
    }

    @GetMapping("/publish4")
    String publish4() {
        User2 user = new User2();
        user.setAge(111);
        user.setName("xieayng");
        //mqListener.listenerShoppeMq(user);
        return "success";
    }
}
