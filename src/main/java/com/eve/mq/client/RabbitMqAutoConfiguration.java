package com.eve.mq.client;

import com.eve.mq.client.rabbit.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @author xieyang
 * @date 19/10/17
 */
@Configuration
@ConditionalOnBean(RabbitmqProperties.class)
public class RabbitMqAutoConfiguration {


    @Bean
    RabbitMqContainerInitializePostProcessor rabbitMqContainerInitializePostProcessor(@Autowired(required = false) List<RabbitMqListerAdvice> advices) {
        return new RabbitMqContainerInitializePostProcessor(advices);
    }

    @Bean
    RabbitMqProducerApiRegistryPostProcessor rabbitMqProducerApiRegistryPostProcessor(){
        return new RabbitMqProducerApiRegistryPostProcessor();
    }

    @Bean
    RabbitMqPublisher mqPublisher(ApplicationContext applicationContext, Jackson2JsonMessageConverter messageConverter) {
        return new RabbitMqPublisher(applicationContext, messageConverter);
    }



    @Bean
    RabbitMqRetryAdvice rabbitMqRetryAdvice(Jackson2JsonMessageConverter messageConverter) {
        return new RabbitMqRetryAdvice(messageConverter);
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
