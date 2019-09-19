package com.example.springboot_quartz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * created by ${user} on 2019/9/11
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.http-client.pool")
public class HttpClientPoolConfig {

    private int maxTotalConnect;

    private int maxConnectPerRout;

    private int connectTimeout;

    private int readTimeout;

    private int connectionRequestTimout;

    private int retryTimes;

    private int keepAliveTime;
}
