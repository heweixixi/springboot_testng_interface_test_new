package com.example.springboot_quartz.exception;

/**
 * created by ${user} on 2019/7/18
 */
public class JobAddException extends RuntimeException{

    public JobAddException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
