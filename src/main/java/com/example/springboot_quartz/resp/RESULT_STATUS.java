package com.example.springboot_quartz.resp;

/**
 * created by ${user} on 2019/7/18
 */
public enum  RESULT_STATUS {
    SUCCESS(200,"成功"),
    SERVER_UNKONW_ERROR(500,"服务异常"),
    PARAM_ERROR(400,"参数异常");

    public  int code;
    public  String msg;

    RESULT_STATUS(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
