package com.example.springboot_quartz.model.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * created by ${user} on 2019/7/25
 */
@Data
@ApiModel("邮件对象")
public class MailPo {
    @NotNull(message = "邮件主题不能为空")
    @ApiParam
    private String subject;
    @NotNull(message = "邮件内容不能为空")
    @ApiParam
    private String content;
}
