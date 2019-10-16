package com.eve;

import com.eve.mq.client.annotation.Producer;
import com.eve.mq.client.annotation.Routekey;
import com.eve.mq.client.annotation.Tenant;

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

    @Tenant
    @Routekey(value = "xie-rout-key")
    void sendUser(User user);

}
