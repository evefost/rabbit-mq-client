package test.com.eve;

import com.eve.TestProducerApi;
import com.eve.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/11/1
 */
public class VirtualProducerTest extends TestBaseService {

    @Autowired
    TestProducerApi api;

    @Autowired
    TestProducerApi2 api2;

    @Test
    public void sendMessage() throws InterruptedException {
        User user = new User();
        user.setAge(129);
        user.setName("xiexieyang");
        api.sendUser(user);
        Thread.sleep(10000);
    }

    @Test
    public void sendMessage2() throws InterruptedException {
        User user = new User();
        user.setAge(32323);
        user.setName("44444444");
        api2.sendUser(user);
        Thread.sleep(10000);
    }

    @Test
    public void test() throws JsonProcessingException {
        User user = new User();
        user.setAge(32323);
        user.setName("44444444");
        JsonMapper mapper = new JsonMapper();
        String s = mapper.writeValueAsString(user);
        System.out.println(s);
    }


}
