package com.eve.mq.client.rabbit;

import com.eve.mq.client.rabbit.annotation.AsRabbitmqProperties;
import com.eve.spring.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.PriorityOrdered;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化rabbit mq containers
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/10/12
 */
public class RabbitMqContainerInitializePostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    protected final Logger logger = LoggerFactory.getLogger(RabbitMqContainerInitializePostProcessor.class);

    private ConfigurableListableBeanFactory beanFactory;

    static Map<String, RabbitmqProperties> rabbitmqPropertiesMap = new ConcurrentHashMap<>();
    private List<RabbitMqListerAdvice> adviceChain;


    public RabbitMqContainerInitializePostProcessor(List<RabbitMqListerAdvice> adviceChain) {
        this.adviceChain = adviceChain;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        List<PropertiesUtils.PropertiesInfo<RabbitmqProperties, AsRabbitmqProperties>> propertiesInfos = null;
        try {
            propertiesInfos = PropertiesUtils.scanProperties(beanFactory, RabbitmqProperties.class, AsRabbitmqProperties.class);
        } catch (Exception e) {
            logger.error("scan rabbit mq properties failure:", e);
        }
        if (propertiesInfos == null || propertiesInfos.isEmpty()) {
            return;
        }
        createMqClient(propertiesInfos);

    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }


    public void createMqClient(List<PropertiesUtils.PropertiesInfo<RabbitmqProperties, AsRabbitmqProperties>> propertiesInfos) {
        for (PropertiesUtils.PropertiesInfo<RabbitmqProperties, AsRabbitmqProperties> propertiesInfo : propertiesInfos) {
            registerRabbitMqClients(propertiesInfo);
        }

    }

    private void registerRabbitMqClients(PropertiesUtils.PropertiesInfo<RabbitmqProperties, AsRabbitmqProperties> propertiesInfo) {
        RabbitmqProperties properties = propertiesInfo.getProperties();
        AsRabbitmqProperties annotation = propertiesInfo.getAnnotation();
        String containerFactoryName = annotation.containerFactory();
        rabbitmqPropertiesMap.put(containerFactoryName, properties);
        String templateName = containerFactoryName + "_template";
        logger.info("register rabbit mq containerFactory beanName:[{}] template beanName:[{}]", containerFactoryName, templateName);

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(properties.getAddress());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setVirtualHost(properties.getVirtualHost());
        connectionFactory.setPublisherConfirms(true);

        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        containerFactory.setPrefetchCount(properties.getPrefetchCount());
        containerFactory.setConcurrentConsumers(properties.getConcurrency());
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setMessageConverter(messageConverter);

        if (adviceChain != null && adviceChain.size() > 0) {
            RabbitMqListerAdvice[] array = new RabbitMqListerAdvice[adviceChain.size()];
            adviceChain.toArray(array);
            containerFactory.setAdviceChain(array);
        }
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);

        beanFactory.registerSingleton(templateName, template);
        beanFactory.registerSingleton(containerFactoryName, containerFactory);

    }


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 10;
    }

}
