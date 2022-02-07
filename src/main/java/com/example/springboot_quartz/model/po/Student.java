package com.example.springboot_quartz.model.po;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * created by ${user} on 2019/8/30
 */
@Data
public class Student {

    private Integer age;
    private String name;

    private List<Subject> subjectList = Lists.newArrayList();

    public Student(Integer age, String name, List<Subject> subjectList) {
        this.age = age;
        this.name = name;
        this.subjectList = subjectList;
    }

    public Student() {
    }
}
