package com.example.springboot_quartz.my_test.po;

public class Constants {
    //注册和登陆case使用（使用正确的手机号注册和登陆验证）
    public static final String VARIABLE_NORMAL_MOBILEPHONE = "${normalMobilephone}";
    //注册和登陆case使用(使用格式不正确的手机号注册和登陆验证)
    public static final String VARIABLE_UN_NORMAL_MOBILEPHONE = "${unnormalMobilephone}";
    //登陆和购物case使用（使用没有注册过的手机号登陆或购物）
    public static final String VARIABLE_NON_EXIST_NORMAL_MOBILEPHONE = "${noExistsMobilephone}";
}
