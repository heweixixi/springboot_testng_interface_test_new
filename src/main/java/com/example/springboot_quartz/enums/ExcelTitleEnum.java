package com.example.springboot_quartz.enums;


import com.google.common.collect.Lists;

import java.util.List;

/**
 * created by ${user} on 2019/7/30
 */
public enum ExcelTitleEnum {

    TITLE1("id","主键"),
    TITLE2("name","名称"),
    TITLE3("age","年龄"),
    TITLE4("gender","性别"),
    TITLE5("grade","班级"),
    TITLE6("address","地址"),
    TITLE7("mobile","电话");

    private String titleCode;
    private String titleName;

    ExcelTitleEnum(String titleCode, String titleName) {
        this.titleCode = titleCode;
        this.titleName = titleName;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public static List<String> getTitleNameList(){
        ExcelTitleEnum[] values = ExcelTitleEnum.values();
        List<String> list = Lists.newArrayList();
        for (ExcelTitleEnum titleEnum : values) {
            list.add(titleEnum.titleName);
        }
        return list;
    }
}
