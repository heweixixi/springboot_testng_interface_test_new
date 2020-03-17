package com.example.springboot_quartz.mapper.base;

import com.example.springboot_quartz.model.dto.QrtzSimpropTriggers;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QrtzSimpropTriggersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_simprop_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int deleteByPrimaryKey(@Param("schedName") String schedName, @Param("triggerName") String triggerName, @Param("triggerGroup") String triggerGroup);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_simprop_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int insert(QrtzSimpropTriggers record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_simprop_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    QrtzSimpropTriggers selectByPrimaryKey(@Param("schedName") String schedName, @Param("triggerName") String triggerName, @Param("triggerGroup") String triggerGroup);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_simprop_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    List<QrtzSimpropTriggers> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_simprop_triggers
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int updateByPrimaryKey(QrtzSimpropTriggers record);
}