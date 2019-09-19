package com.example.springboot_quartz.model.po;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * created by ${user} on 2019/8/30
 */
@Data
public class Teacher {

    private Integer age;
    private String name;

    private List<Student> studentList = Lists.newArrayList();

    public Teacher(Integer age, String name, List<Student> studentList) {
        this.age = age;
        this.name = name;
        this.studentList = studentList;
    }
}
