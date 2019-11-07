package com.example.springboot_quartz.mq.consumer;

import com.example.springboot_quartz.mq.dto.OrderPushEvent;
import com.example.springboot_quartz.mq.listenerContainer.OrderListenerContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * created by ${user} on 2019/11/7
 */
@Component
@Slf4j
public class OrderConsumer {

    @RabbitListener(queues = OrderListenerContainer.QUEUE_NAME,
            group = OrderListenerContainer.GROUP_NAME,
            containerFactory = OrderListenerContainer.CONTAINER_FACTORY)
    @RabbitHandler
    public void process(Message<OrderPushEvent> message){
        Integer orderId = message.getPayload().getOrderId();
        Integer sellerId = message.getPayload().getSellerId();
        log.info("消息者尝试消息");
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
//        int error = 1/0;
        log.info("消费者【{}】接收到的消息，sellerId[{}],orderId[{}]",OrderConsumer.class.getSimpleName(),sellerId,orderId);
        // TODO: 2019/11/8 更新数据库消息记录消费状态为成功，并且更新消费次数 
    }
}
