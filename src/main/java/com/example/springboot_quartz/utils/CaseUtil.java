package com.example.springboot_quartz.utils;

import com.example.springboot_quartz.model.case_po.Case;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by ${user} on 2019/9/16
 */
@Slf4j
//@Component
public class CaseUtil {
    public static List<Case> caseList = new ArrayList<>();
    public static Map<String,Integer> fieldNameAndColumNumMap = new HashMap<>();

    //初始化加载excel数据，填充caseList数据
    static {
        log.info("=====================执行CaseUtil static方法=====================================");
        ExcelUtil.loadBeans("src/test/resources/rest_info.xlsx",1,Case.class);
    }
}
