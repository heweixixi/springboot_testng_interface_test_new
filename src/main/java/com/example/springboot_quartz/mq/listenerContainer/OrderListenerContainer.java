package com.example.springboot_quartz.mq.listenerContainer;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by ${user} on 2019/11/7
 */
@Configuration
public class OrderListenerContainer extends RabbitListenerConfig {

    public final static String QUEUE_NAME = "hewei_queue";

    public final static String GROUP_NAME = "order_group";
;
    public final static String CONTAINER_FACTORY = EXCHANGE+GROUP_NAME+QUEUE_NAME+"simpleListenerContainerFactory";

    @Bean(name = CONTAINER_FACTORY)
    public SimpleRabbitListenerContainerFactory listenerContainerFactory(@Qualifier("connectFactory") ConnectionFactory connectionFactory, @Qualifier("rabbitTemplate")RabbitTemplate rabbitTemplate){
        SimpleRabbitListenerContainerFactory factory = rabbitListenerContainerFactory(connectionFactory);
        //设置重试机制
        factory.setAdviceChain(rejectAndDontRequeueInterceptor(rabbitTemplate));
        return factory;
    }

}
