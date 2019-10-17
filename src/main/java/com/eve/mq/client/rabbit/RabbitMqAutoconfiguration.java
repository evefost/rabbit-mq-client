package com.eve.mq.client.rabbit;

import com.eve.MqListener;
import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by xieyang on 19/10/17.
 */
@Configuration
public class RabbitMqAutoconfiguration implements InitializingBean{




    @Bean
    RabbitMqContainerInitializePostProcessor rabbitMqContainerInitializePostProcessor(@Autowired(required = false) RabbitMqListerAdvice[] advice){

        return new RabbitMqContainerInitializePostProcessor(advice);
    }

    @Bean
    RabbitMqProducerApiRegistryPostProcessor rabbitMqProducerApiRegistryPostProcessor(){
        return new RabbitMqProducerApiRegistryPostProcessor();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.printf("");
    }
}
