package com.example.springboot_quartz.mq.config;

import com.example.springboot_quartz.mq.callback.MessageConfirmCallback;
import com.example.springboot_quartz.mq.callback.MessageReturnCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * created by ${user} on 2019/11/4
 */
@Configuration
//@ConfigurationProperties(prefix = "spring.rabbitmq")
//@PropertySource("classpath:application.yml")
@Slf4j
public class RabbitConnectConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    @Autowired
    MessageConfirmCallback messageConfirmCallback;
    @Autowired
    MessageReturnCallback messageReturnCallback;

    @Bean(name = "connectFactory")
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setVirtualHost(virtualHost);
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setPublisherConfirms(true);
        factory.setPublisherReturns(true);
        return factory;
    }

    /**
     * ConfirmCallback 和 ReturnCallback 的区别
     * Returns are when the broker returns a message because it's undeliverable
     * (no matching bindings on the exchange to which the message was published,
     * and the mandatory bit is set).
     *
     * Confirms are when the broker sends an ack back to the publisher,
     * indicating that a message was successfully routed.
     *
     * confirm 主要是用来判断消息是否有正确到达交换机，如果有，那么就 ack 就返回 true；如果没有，则是 false
     *
     * return 则表示如果你的消息已经正确到达交换机，但是后续处理出错了(没有到达队列)，那么就会回调 return，并且把信息送回给你
     * （前提是需要设置了 Mandatory，不设置那么就丢弃）；如果消息没有到达交换机，那么不会调用 return 的东西。
     * @return
     */
    @Bean(name = "rabbitTemplate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //消息发送broker回执消息
        rabbitTemplate.setConfirmCallback(messageConfirmCallback);
        //消息发送queue确认回执消息
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback(messageReturnCallback);
        return rabbitTemplate;
    }
}
