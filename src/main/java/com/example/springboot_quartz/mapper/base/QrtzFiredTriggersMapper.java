package com.example.springboot_quartz.mapper.base;

import com.example.springboot_quartz.model.dto.QrtzFiredTriggers;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QrtzFiredTriggersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_fired_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int deleteByPrimaryKey(@Param("schedName") String schedName, @Param("entryId") String entryId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_fired_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int insert(QrtzFiredTriggers record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_fired_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    QrtzFiredTriggers selectByPrimaryKey(@Param("schedName") String schedName, @Param("entryId") String entryId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_fired_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    List<QrtzFiredTriggers> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_fired_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int updateByPrimaryKey(QrtzFiredTriggers record);
}