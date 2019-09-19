package com.example.springboot_quartz.mapper.customer;

import com.example.springboot_quartz.model.po.InterfaceTestPo;
import com.example.springboot_quartz.model.po.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * created by ${user} on 2019/9/16
 */
@Mapper
public interface ResultMapper {

    List<Student> getStudentList();

    String getMaxAgeStudent();

    int addStudent(InterfaceTestPo student);

    int deleteStudent(@Param("id") Integer id);

    InterfaceTestPo getStudent(@Param("id") Integer id);
}
