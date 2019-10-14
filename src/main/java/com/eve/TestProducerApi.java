package com.eve;

import com.eve.mq.client.annotation.Producer;
import com.eve.mq.client.annotation.Routekey;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/14
 */
@Producer(containerFactory = "container_1")
public interface TestProducerApi {

    @Routekey(exchange = "xie_test", value = "xie-rout-key")
    void sendUser(User user);

}