package com.example.springboot_quartz.model.dto;

import lombok.Data;

@Data
public class QrtzSimpleTriggers {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_simple_triggers.SCHED_NAME
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private String schedName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_simple_triggers.TRIGGER_NAME
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private String triggerName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_simple_triggers.TRIGGER_GROUP
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private String triggerGroup;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_simple_triggers.REPEAT_COUNT
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private Long repeatCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_simple_triggers.REPEAT_INTERVAL
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private Long repeatInterval;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column qrtz_simple_triggers.TIMES_TRIGGERED
     *
     * @mbggenerated Thu Jul 18 15:57:05 CST 2019
     */
    private Long timesTriggered;
}