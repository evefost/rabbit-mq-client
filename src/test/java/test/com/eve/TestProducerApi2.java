package test.com.eve;

import com.eve.User;
import com.eve.mq.client.rabbit.annotation.Producer;
import com.eve.mq.client.rabbit.annotation.RouteKey;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/14
 */
@Producer(exchange = "xie_test", containerFactory = "container_1")
public interface TestProducerApi2 {

    @RouteKey(value = "xie-rout-key")
    void sendUser(User user);

}
