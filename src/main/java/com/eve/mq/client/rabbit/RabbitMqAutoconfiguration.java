package com.eve.mq.client.rabbit;

import com.eve.mq.client.RabbitMqListerTenantAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by xieyang on 19/10/17.
 */
@Configuration
public class RabbitMqAutoconfiguration {


    @Bean
    RabbitMqContainerInitializePostProcessor rabbitMqContainerInitializePostProcessor(@Autowired(required = false) List<RabbitMqListerAdvice> advice) {
        return new RabbitMqContainerInitializePostProcessor(advice);
    }

    @Bean
    RabbitMqProducerApiRegistryPostProcessor rabbitMqProducerApiRegistryPostProcessor(){
        return new RabbitMqProducerApiRegistryPostProcessor();
    }

    @Bean
    RabbitMqPublisher mqPublisher(ApplicationContext applicationContext) {
        return new RabbitMqPublisher(applicationContext);
    }

    @Bean
    RabbitMqListerTenantAdvice rabbitMqListerTenantAdvice() {
        return new RabbitMqListerTenantAdvice();
    }

}
