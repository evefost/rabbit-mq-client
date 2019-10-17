package com.eve;

import com.eve.mq.client.rabbit.annotation.Producer;
import com.eve.mq.client.rabbit.annotation.Routekey;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/14
 */
@Producer(exchange = "xie_test", containerFactory = "container_2")
public interface TestProducerApi {

    @Routekey(value = "xie-rout-key")
    void sendUser(User user);

}
