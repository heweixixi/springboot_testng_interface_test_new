package com.example.springboot_quartz.controller;

import com.example.springboot_quartz.check.MyCheckerGroup;
import com.example.springboot_quartz.resp.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by ${user} on 2019/7/25
 */
@RestController
@RequestMapping("checker")
@Api("check校验")
public class CheckerController {

    @Autowired
    MyCheckerGroup myChecker;


    @PostMapping
    @ApiOperation("check方法")
    public Result check(Object o){
        return myChecker.check(o);
    }

}
