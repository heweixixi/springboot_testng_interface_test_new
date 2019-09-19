package com.example.springboot_quartz.exception;

import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * created by ${user} on 2019/7/18
 */
@ControllerAdvice
public class GlobleExceptionHandler {


    @ExceptionHandler(JobAddException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result JobAddExceptionHandler(JobAddException exception){
        return new Result(RESULT_STATUS.SERVER_UNKONW_ERROR.code,exception.getMessage());
    }

    @ExceptionHandler(JobUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ResponseBody
    public Result jobUpdateExceptionHandler(JobUpdateException exception){
        return new Result(RESULT_STATUS.SERVER_UNKONW_ERROR.code,exception.getMessage());
    }

}
