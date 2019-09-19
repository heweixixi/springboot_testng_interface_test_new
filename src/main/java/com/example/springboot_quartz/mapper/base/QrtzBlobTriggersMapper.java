package com.example.springboot_quartz.mapper.base;

import com.example.springboot_quartz.model.dto.QrtzBlobTriggers;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QrtzBlobTriggersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_blob_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int deleteByPrimaryKey(@Param("schedName") String schedName, @Param("triggerName") String triggerName, @Param("triggerGroup") String triggerGroup);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_blob_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int insert(QrtzBlobTriggers record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_blob_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    QrtzBlobTriggers selectByPrimaryKey(@Param("schedName") String schedName, @Param("triggerName") String triggerName, @Param("triggerGroup") String triggerGroup);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_blob_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    List<QrtzBlobTriggers> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_blob_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int updateByPrimaryKey(QrtzBlobTriggers record);
}