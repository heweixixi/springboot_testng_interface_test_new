package com.example.springboot_quartz.mq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * created by ${user} on 2019/11/7
 * 消息是否到达exchange
 * 做相应的逻辑处理
 * ack:true 成功
 * 更新消息为发送成功
 * ack:false 失败
 * 更新消息为发送失败
 *
 */
@Component
@Slf4j
public class MessageConfirmCallback implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //如果消息成功发送到exchange
        if (ack){
            log.info("消息已成功发送到交换机");
            // TODO: 2019/11/8 更新数据库的消息记录为发送成功
        }else {
            log.info("消息发送的到交换机失败,消息id：【{}】,失败的原因：【{}】",correlationData.getId(),cause);
            //更新消息发送失败
            // TODO: 2019/11/8 更新数据库的消息记录为发送失败
        }
    }
}
