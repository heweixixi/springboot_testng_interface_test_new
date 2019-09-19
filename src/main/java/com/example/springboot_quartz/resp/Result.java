package com.example.springboot_quartz.resp;

import lombok.Data;
import lombok.ToString;

/**
 * created by ${user} on 2019/7/18
 */
//@Data
@ToString
public class Result<T> {

    private T data;

    private Integer code = RESULT_STATUS.SUCCESS.code;

    private String msg = RESULT_STATUS.SUCCESS.msg;

    public Result() {
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(T data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public Result(RESULT_STATUS resultStatus){
        this.code = resultStatus.code;
        this.msg = resultStatus.msg;
    }


    public static <T>Result<T> error(int code,String msg){
        return new Result<>(code,msg);
    }

    public static <T>Result<T> error(T data,int code,String msg){
        return new Result<>(data,code,msg);
    }

    public static <T>Result<T> success(int code,String msg){
        return new Result<>(code,msg);
    }

    public static <T>Result<T> success(T data,int code,String msg){
        return new Result<>(data,code,msg);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
