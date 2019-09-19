package com.example.springboot_quartz.model.po;

import lombok.Data;

import java.sql.Timestamp;

/**
 * created by ${user} on 2019/9/17
 */
@Data
public class InterfaceTestPo {

    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private Timestamp time;
    private boolean is_student;
    private String mobile;
}
