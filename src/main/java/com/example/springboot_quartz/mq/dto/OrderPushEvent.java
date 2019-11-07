package com.example.springboot_quartz.mq.dto;

import lombok.Data;

@Data
public class OrderPushEvent {

    private Integer orderId;
    private Integer sellerId;
    private String msgId;
}
