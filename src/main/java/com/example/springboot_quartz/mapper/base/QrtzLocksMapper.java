package com.example.springboot_quartz.mapper.base;

import com.example.springboot_quartz.model.dto.QrtzLocks;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QrtzLocksMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_locks
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int deleteByPrimaryKey(@Param("schedName") String schedName, @Param("lockName") String lockName);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_locks
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    int insert(QrtzLocks record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table qrtz_locks
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    List<QrtzLocks> selectAll();
}