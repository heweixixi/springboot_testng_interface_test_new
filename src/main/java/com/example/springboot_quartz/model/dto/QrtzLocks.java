package com.example.springboot_quartz.model.dto;

import lombok.Data;

@Data
public class QrtzLocks {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_locks.SCHED_NAME
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private String schedName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_locks.LOCK_NAME
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private String lockName;
}