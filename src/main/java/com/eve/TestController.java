package com.eve;

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

//    @Resource(name = "bbb")
//    RabbitTemplate template2;

    @Autowired
    private TestProducerApi producerApi;

    @GetMapping("/publish")
    String publish(){
        User user = new User();
        user.setAge(111);
        user.setName("xieayng");
        //template.convertAndSend("xie_test","xie-rout-key",user);
        producerApi.sendUser(user);
        return "success";
    }

    @GetMapping("/publish2")
    String publish2(){
        User user = new User();
        user.setAge(111);
        user.setName("xieayng");
        //template2.convertAndSend("xie_test","xie-rout-key",user);
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
        mqListener.listenerShoppeMq(user);
        return "success";
    }
}
