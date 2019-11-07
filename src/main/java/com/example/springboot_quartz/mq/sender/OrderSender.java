package com.example.springboot_quartz.mq.sender;

import com.example.springboot_quartz.mq.dto.OrderPushEvent;
import com.example.springboot_quartz.mq.listenerContainer.OrderListenerContainer;
import com.example.springboot_quartz.resp.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by ${user} on 2019/11/7
 */
@RestController
@RequestMapping("/order")
@Slf4j
@Api("消息发送者")
public class OrderSender {

    @Autowired
    @Qualifier(value = "rabbitTemplate")
    RabbitTemplate rabbitTemplate;


    @RequestMapping(name = "/send",method = RequestMethod.POST)
    @ApiOperation("发送消息")
    public Result orderPush(@RequestBody OrderPushEvent event){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i=0;i<100;i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        rabbitTemplate.convertAndSend(OrderListenerContainer.EXCHANGE,"hewei_rounting_key",event,new CorrelationData(event.getMsgId()));
                    } catch (AmqpException e) {
                        log.error("消息发送失败");
//                    return Result.error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"消息发送失败");
                    }
                }
            });
            log.info("消息[{}]已发送",String.valueOf(i));
        }
        return Result.success(HttpStatus.SC_OK,"消息发送成功");
    }

    @PostMapping("/sendSingle")
    @ApiOperation("发送单条消息")
    public Result orderPushSingle(@RequestBody OrderPushEvent event){
        try {
            rabbitTemplate.convertAndSend(OrderListenerContainer.EXCHANGE,"hewei_rounting_key",event,new CorrelationData(event.getMsgId()));
        } catch (AmqpException e) {
            log.error("消息发送失败");
                return Result.error(HttpStatus.SC_INTERNAL_SERVER_ERROR,"消息发送失败");
        }
        return Result.success(HttpStatus.SC_OK,"消息发送成功");
    }
}
