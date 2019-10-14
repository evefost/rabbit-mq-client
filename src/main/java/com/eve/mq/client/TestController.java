package com.eve.mq.client;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @Resource(name = "aaa")
    RabbitTemplate template;

    @Resource(name = "bbb")
    RabbitTemplate template2;

    @GetMapping("/publish")
    String publish(){
        User user = new User();
        user.setAge(111);
        user.setName("xieayng");
        template.convertAndSend("xie_test","xie-rout-key",user);
        return "success";
    }

    @GetMapping("/publish2")
    String publish2(){
        User user = new User();
        user.setAge(111);
        user.setName("xieayng");
        template2.convertAndSend("xie_test","xie-rout-key",user);
        return "success";
    }
}
