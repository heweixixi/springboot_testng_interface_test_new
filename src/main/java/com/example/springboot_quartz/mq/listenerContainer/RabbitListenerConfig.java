package com.example.springboot_quartz.mq.listenerContainer;

import org.aopalliance.intercept.Interceptor;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

/**
 * created by ${user} on 2019/11/5
 * 公用的简单消息监听容器，具体的消息监听容器可以继承该容器，然后定制化子消息监听容器
 */
public class RabbitListenerConfig {

    /**
     * 队列绑定的交换机
     * @param factory
     * @return
     */
    public final static  String EXCHANGE = "hewei_exchange";

    /**
     * 死信交换机
     */
    public final static String ERROR_EXCHANGE = "test_dlx_exchange";

    /**
     * 死信rounting
     */
    public final static String ERROR_ROUNTING_KEY = "dlx.#";



    protected SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(8);
        factory.setMaxConcurrentConsumers(8);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    /**
     * Interceptor for 重试一定次数后丢弃消息
     * 核心业务使用这个策略时，一定要有补偿逻辑才可以
     * 如果不适用重试丢弃策略，那么如果消息有异常，那么消息机会永远的重试下去，造成消息阻塞
     *
     * initialInterval 最初重试间隔毫秒数
     *max-attempts: 最多重试 次数
     *multiplier: 每次重试失败后，重试间隔所增加的倍数
     * max-interval: 最长重试间隔毫秒数
     * @return
     *
     *
     */
    protected Interceptor rejectAndDontRequeueInterceptor(RabbitTemplate rabbitTemplate){
        RetryOperationsInterceptor interceptor = RetryInterceptorBuilder.stateless().
                maxAttempts(2).
                backOffOptions(1000, 2, 5000).
                recoverer(new RepublishMessageRecoverer(rabbitTemplate,ERROR_EXCHANGE,ERROR_ROUNTING_KEY)).
                build();
        return interceptor;
    }
}
