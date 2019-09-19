package com.example.springboot_quartz.controller;

import com.alibaba.druid.util.StringUtils;
import com.example.springboot_quartz.model.vo.JobDto;
import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import com.example.springboot_quartz.service.QuartzJobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * created by ${user} on 2019/7/18
 */
@RestController
@RequestMapping("quartz")
@Api(value = "quartz配置",position = 2)
@Slf4j
public class QuartzController {

    @Autowired
    QuartzJobService quartzJobService;

    /**
     * 新增job
     * @param jobDto
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增job")
    public Result addJob(@Validated(JobDto.addGroup.class) @RequestBody JobDto jobDto){
        Result result = new Result();
        Gson gson = new Gson();
        JobDataMap jobDataMap = gson.fromJson(jobDto.getJobData(), new TypeToken<JobDataMap>() {
        }.getType());
        log.info("jobDataMap:{}",jobDataMap);
        quartzJobService.addJob(jobDto.getJobAllPath(),jobDto.getJobName(),jobDto.getJobGroupName(),jobDto.getCronExpression(),jobDataMap);
        result.setCode(RESULT_STATUS.SUCCESS.code);
        result.setMsg(RESULT_STATUS.SUCCESS.msg);
        return result;
    }

    @PostMapping("/pause")
    @ApiOperation("暂停任务")
    public Result pauseJob(@Validated(JobDto.pauseGroup.class) @RequestBody JobDto jobDto){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        quartzJobService.pauseJob(jobDto.getJobName(),jobDto.getJobGroupName());
        return result;
    }

    @PostMapping("/resume")
    @ApiOperation("恢复任务")
    public Result resumeJob(@Validated(JobDto.pauseGroup.class) @RequestBody JobDto jobDto){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        quartzJobService.resumeJob(jobDto.getJobName(),jobDto.getJobGroupName());
        return result;
    }

    @PostMapping("/delete")
    @ApiOperation("删除任务")
    public Result deleteJob(@Validated(JobDto.pauseGroup.class) @RequestBody JobDto jobDto){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        quartzJobService.deleteJob(jobDto.getJobName(),jobDto.getJobGroupName(),jobDto.getJobName(),jobDto.getJobGroupName());
        return result;
    }
    @PostMapping("/update")
    @ApiOperation("更新任务")
    public Result updateJob(@Validated(JobDto.pauseGroup.class) @RequestBody JobDto jobDto){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        Gson gson = new Gson();
        if (Objects.nonNull(jobDto.getJobData())){
            JobDataMap map = gson.fromJson(jobDto.getJobData(), new TypeToken<JobDataMap>() {
            }.getType());
            quartzJobService.updateJob(jobDto.getJobName(),jobDto.getJobGroupName(),jobDto.getCronExpression(),map);
        }else {
            quartzJobService.updateJob(jobDto.getJobName(),jobDto.getJobGroupName(),jobDto.getCronExpression());
        }
        return result;
    }

    @GetMapping("/getJobList")
    @ApiOperation("任务列表")
    public Result getAllJobList(){
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        List<Map<String, Object>> allJobList = quartzJobService.getAllJobList();
        result.setData(allJobList);
        return result;
    }

}
