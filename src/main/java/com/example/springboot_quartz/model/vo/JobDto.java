package com.example.springboot_quartz.model.vo;

import com.example.springboot_quartz.annotataion.JobDtoContraint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * created by ${user} on 2019/7/18
 */
@Data
@JobDtoContraint(groups = JobDto.addGroup.class)
@ApiModel(value = "jobDto")
public class JobDto {

    public interface addGroup{}
    public interface updateGroup{}
    public interface pauseGroup{}
    public interface resumeGroup{}
    public interface deleteGroup{}

    /**
     * job全路径
     */
    @NotNull(message = "jobAllPath不能为空",groups = {JobDto.addGroup.class})
    @ApiParam("job全路径")
    private String jobAllPath;
    /**
     * job名称
     */
    @NotBlank(message = "jobName不能为空",groups = {JobDto.addGroup.class,JobDto.pauseGroup.class,resumeGroup.class,deleteGroup.class,updateGroup.class})
    @ApiParam("job名称")
    private String jobName;
    /**
     * job组名称
     */
    @NotEmpty(message = "jobGroupName不能为空",groups = {JobDto.addGroup.class,JobDto.pauseGroup.class,resumeGroup.class,deleteGroup.class,updateGroup.class})
    @ApiParam("job组名称")
    private String jobGroupName;
    /**
     * cron表达式
     */
    @NotBlank(message = "cronExpression不能为空",groups = JobDto.addGroup.class)
    @ApiParam("cron表达式")
    private String cronExpression;
    /**
     * job参数
     */
    @ApiParam("job参数")
    private String jobData;
}
