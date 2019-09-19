package com.example.springboot_quartz.model.po;

import lombok.Data;

/**
 * created by ${user} on 2019/8/30
 */
@Data
public class Subject {

    private String yunwen;
    private String math;

    public Subject(String yunwen, String math) {
        this.yunwen = yunwen;
        this.math = math;
    }
}
