package com.example.springboot_quartz.controller;

import com.example.springboot_quartz.mapper.customer.ResultMapper;
import com.example.springboot_quartz.model.po.InterfaceTestPo;
import com.example.springboot_quartz.model.po.Student;
import com.example.springboot_quartz.resp.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * created by ${user} on 2019/9/17
 */
@RestController
@Api(value = "数据测试接口")
public class StudentController {

    @Autowired
    ResultMapper resultMapper;


    @PostMapping("/add")
    @ApiOperation(value = "新增接口")
    public Result addStudent(@RequestBody InterfaceTestPo student){
        resultMapper.addStudent(student);
        return Result.success(HttpStatus.OK.value(),"成功");
    }

    @PostMapping("/delete")
    @ApiOperation("删除接口")
    public Result deleteStudent(@RequestBody Integer id){
        resultMapper.deleteStudent(id);
        return Result.success(HttpStatus.OK.value(),"成功");
    }

    @GetMapping("/get")
    public Result<InterfaceTestPo> getStudent(Integer id){
        InterfaceTestPo student = resultMapper.getStudent(id);
        return Result.success(student,HttpStatus.OK.value(),"成功");
    }

    //1、参数object带有@requestBody注解
    //2、参数simple 带有@requestBody注解simple
    //3、不带有@requestBody注解的参数，使用url拼接的方式  simple2
}
