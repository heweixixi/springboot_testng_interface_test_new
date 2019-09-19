package com.example.springboot_quartz.constains;

/**
 * created by ${user} on 2019/9/17
 */
public class Constains {

    public static final String VARIABLE_NORMAL_MOBILEPHONE = "${normalMobilephone}";
    public static final String VARIABLE_UN_NORMAL_MOBILEPHONE = "${unNormalMobilephone}";
    public static final String VARIABLE_NON_EXIST_NORMAL_MOBILEPHONE = "${noExistsMobilephone}";
    /**
     * 接口参数为vo
     * 接口：vo
     * excel：{}
     */
    public static final String OBJECT = "object";

    /**
     * 接口参数为带有@RequestBody的基本类型
     * 接口:(Integer id)
     * excel:{"id":"1"}
     * 使用new StringEntity("1","utf-8")
     */
    public static final String SIMPLE = "simple";

    /**
     * 接口参数为不带有@RequestBody注解的基本类型
     * 比如：
     * 接口：（Integer id）或者（Integer id,String name）
     * excel：{"id":"1"}或者{"id":"1","name":"a"}
     */
    public static final String NO_ANNOTATIONS_SIMPLE = "noAnnotationsSimple";

    /**
     * 接口参数为不带@RequestBody注解的复杂参数
     * 比如：
     * 接口：(Integer id,Student student)
     * excel参数：[{"id":"1"},{"name":"a"}]
     */
    public static final String NO_ANNOTATIONS_SIMPLE_AND_OBJECT = "noAnnotationsSimpleAndObject";

}
