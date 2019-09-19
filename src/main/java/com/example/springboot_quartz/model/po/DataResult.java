package com.example.springboot_quartz.model.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * created by ${user} on 2019/9/12
 */
@Data
@ApiModel("接口对象")
public class DataResult<T> implements Serializable {

    private String code;

    private String message;

    private T data;
}
