package com.example.springboot_quartz.exception;

/**
 * created by ${user} on 2019/7/18
 */
public class JobUpdateException extends RuntimeException {

    public JobUpdateException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
