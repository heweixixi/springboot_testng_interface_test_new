package com.example.springboot_quartz.job;

import ch.qos.logback.core.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.TimeUnit;

/**
 * created by ${user} on 2019/7/18
 */
@Slf4j
public class MyJob implements Job {


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long startTime = System.currentTimeMillis();
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        log.info("jobDataMap:{}",jobDataMap.get("beforeday"));
        log.info("MyJob开始执行了");
        try {
            log.info("休息了1秒");
            Thread.currentThread().sleep(TimeUnit.SECONDS.toSeconds(1));
        } catch (InterruptedException e) {
            log.error("error:{}",e);
        }
        log.info("MyJob执行结束");
        long endTime = System.currentTimeMillis();
        log.info("该任务共执行了{}秒",endTime-startTime);
    }
}
