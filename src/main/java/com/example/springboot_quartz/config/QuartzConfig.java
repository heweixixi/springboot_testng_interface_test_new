package com.example.springboot_quartz.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;

/**
 * created by ${user} on 2019/7/17
 * quartz配置类
 */
@Configuration
public class QuartzConfig {

    @Autowired
    TaskJobFactory taskJobFactory;

    @Bean("schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        //获取配置属性
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        //创建SchedulerFactoryBean
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setQuartzProperties(propertiesFactoryBean.getObject());
        schedulerFactoryBean.setJobFactory(taskJobFactory);
        return schedulerFactoryBean;

    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     * @return
     */
    @Bean("scheduler")
    public Scheduler scheduler(@Qualifier("schedulerFactoryBean") SchedulerFactoryBean schedulerFactoryBean){
        return schedulerFactoryBean.getScheduler();
    }

}
