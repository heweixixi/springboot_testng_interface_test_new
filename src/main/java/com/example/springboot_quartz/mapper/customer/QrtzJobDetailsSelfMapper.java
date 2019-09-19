package com.example.springboot_quartz.mapper.customer;

import com.example.springboot_quartz.model.dto.QrtzJobDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * created by ${user} on 2019/7/18
 */
@Mapper
public interface QrtzJobDetailsSelfMapper {

    QrtzJobDetails getQrtzJobDetailsMapperByNameAndGroup(@Param("jobName")String jobName,@Param("jobGroup") String jobGroup);
}
