package com.example.springboot_quartz.mq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * created by ${user} on 2019/11/7
 * 消息发送到queue失败，返回消息提示
 */
@Component
@Slf4j
public class MessageReturnCallback implements RabbitTemplate.ReturnCallback {

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息没有发送到绑定到【{}】的queue",routingKey);
        log.info("消息主体【{}】",message.getBody().toString());
        log.info("交换机【{}】",exchange);
        log.info("routingkey【{}】",routingKey);
    }
}
