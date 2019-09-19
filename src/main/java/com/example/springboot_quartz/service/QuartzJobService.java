package com.example.springboot_quartz.service;

import com.example.springboot_quartz.exception.JobAddException;
import com.example.springboot_quartz.exception.JobUpdateException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by ${user} on 2019/7/17
 */
@Service
@Slf4j
public class QuartzJobService {

    @Resource(name = "scheduler")
    Scheduler scheduler;

    /**
     * 创建job，可传参
     * @param jobPath job 全路径
     * @param jobName job名称
     * @param jobGroupName job组名称
     * @param cronExpression cron表达式
     * @param argMap job参数
     */
    public void addJob(String jobPath, String jobName, String jobGroupName, String cronExpression, Map<String,Object> argMap){
        try {
            //启动调度器
            scheduler.start();
            //构建jobDetail信息
            JobDetail jobDetail = JobBuilder.newJob(((Job)Class.forName(jobPath).newInstance()).getClass()).withIdentity(jobName, jobGroupName).build();
            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(cronScheduleBuilder).build();
            //获得jobDataMap
            if (argMap != null){
                jobDetail.getJobDataMap().putAll(argMap);
            }
            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (Exception e) {
            log.error("addJob error,message:{}",e.getMessage());
            throw new JobAddException("addJob error");

        }
    }

    /**
     * 添加job，不带参数
     * @param jobPath
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     */
    public void addJob(String jobPath, String jobName, String jobGroupName, String cronExpression){
        addJob(jobPath, jobName, jobGroupName, cronExpression,null);
    }

    /**
     * 暂停job
     * @param jobName
     * @param jobGroupName
     */
    public void pauseJob(String jobName,String jobGroupName){
        try {
            scheduler.pauseJob(JobKey.jobKey(jobName,jobGroupName));
        } catch (SchedulerException e) {
            log.error("pauseJob error,message:{}",e.getMessage());
        }
    }

    /**
     * 恢复job
     * @param jobName
     * @param jobGroupName
     */
    public void resumeJob(String jobName,String jobGroupName){
        try {
            scheduler.resumeJob(JobKey.jobKey(jobName,jobGroupName));
        } catch (SchedulerException e) {
            log.error("resumeJob error,message:{}",e.getMessage());
        }
    }

    /**
     * 删除job
     * @param jobName
     * @param jobGroupName
     */
    public void deleteJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName){
        try {
            //暂停触发器
            scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName,triggerGroupName));
            //移除触发器
            scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName,triggerGroupName));
            //删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName,jobGroupName));
        } catch (SchedulerException e) {
            log.error("deleteJob error,message:{}",e.getMessage());
        }
    }

    /**
     * 更新job,更新频率和参数
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     * @param argMap
     */
    public void updateJob(String jobName,String jobGroupName,String cronExpression,Map<String,Object> argMap){
        try {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
//            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName).withSchedule(cronScheduleBuilder).build();
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
            trigger = trigger.getTriggerBuilder().withIdentity(jobName, jobGroupName).withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
            if (argMap != null){
                jobDetail.getJobDataMap().putAll(argMap);
            }
            scheduler.rescheduleJob(triggerKey,trigger);
        } catch (Exception e) {
            log.error("updateJob erro,message:{}",e.getMessage());
            throw new JobUpdateException("updateJob error");
        }
    }

    /**
     * 更新job，只更新频率
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     */
    public void updateJob(String jobName,String jobGroupName,String cronExpression){
        updateJob(jobName,jobGroupName,cronExpression,null);
    }

    /**
     * 更新job，只更新参数
     * @param jobName
     * @param jobGroupName
     * @param argsMap
     */
    public void updateJob(String jobName,String jobGroupName,Map<String,Object> argsMap){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            if (argsMap != null){
                trigger.getJobDataMap().putAll(argsMap);
            }
            scheduler.rescheduleJob(triggerKey,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动所有任务
     */
    public void startAllJobs(){
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("startAllJobs error,message:{}",e.getMessage());
        }
    }

    /**
     * 关闭所有任务
     */
    public void shutDownAllJobs(){
        try {
            if (!scheduler.isShutdown()){
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            log.error("shutDownAllJobs error,message:{}",e.getMessage());
        }
    }

    /**
     * 获取所有任务列表
     * 指那些已经添加到quartz调度器的任务，因为quartz并没有直接提供这样的查询接口，所以我们需要结合JobKey和Trigger来实现
     * @return
     */
    public List<Map<String,Object>> getAllJobList(){
        List<Map<String,Object>> list = Lists.newArrayList();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggerList = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggerList) {
                    Map<String,Object> jobMap = Maps.newHashMap();
                    jobMap.put("jobName",jobKey.getName());
                    jobMap.put("jobGroupName",jobKey.getGroup());
                    jobMap.put("trigger",trigger.getKey());
                    jobMap.put("jobStatus",scheduler.getTriggerState(trigger.getKey()));
                    if (trigger instanceof CronTrigger){
                        String cronExpression = ((CronTrigger) trigger).getCronExpression();
                        jobMap.put("cronException",cronExpression);
                    }
                    list.add(jobMap);
                }
            }
        } catch (SchedulerException e) {
            log.error("getAllJobList errot,message:{}",e.getMessage());
            return null;
        }
        return list;
    }

    /**
     * 获取运行中的任务列表
     * @return
     */
    public List<Map<String,Object>> getCurrentRunJobList(){
        List<Map<String,Object>> jobList = Lists.newArrayList();
         try {
            List<JobExecutionContext> currentlyExecutingJobList = scheduler.getCurrentlyExecutingJobs();
             for (JobExecutionContext jobExecutionContext : currentlyExecutingJobList) {
                 Map<String,Object> jobMap = Maps.newHashMap();
                 JobKey key = jobExecutionContext.getJobDetail().getKey();
                 jobMap.put("jobName",key.getName());
                 jobMap.put("jobGroupName",key.getGroup());
                 jobMap.put("trigger",jobExecutionContext.getTrigger().getKey());
                 jobMap.put("triggerStatus",scheduler.getTriggerState(jobExecutionContext.getTrigger().getKey()));
                 if (jobExecutionContext.getTrigger() instanceof CronTrigger){
                     String cronExpression = ((CronTrigger) jobExecutionContext.getTrigger()).getCronExpression();
                     jobMap.put("cronExpression",cronExpression);
                 }
                 jobList.add(jobMap);
             }
        } catch (SchedulerException e) {
            log.error("getCurrentRunJobList error,message:{}",e.getMessage());
            return null;
        }
        return jobList;
    }
}
