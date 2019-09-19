package com.example.springboot_quartz.check;

import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * created by ${user} on 2019/7/25
 */
@Service
@Slf4j
public class TestChecker extends AbstractChecker{

    @Override
    public Result check(Object o) {
        log.info("TestChecker校验成功");
        return new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
    }
}
