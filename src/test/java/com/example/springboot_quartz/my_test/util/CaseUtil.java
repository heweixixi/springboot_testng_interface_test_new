package com.example.springboot_quartz.my_test.util;

import com.example.springboot_quartz.my_test.po.Case;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;

/**
 * 1、caseUtil用来获取case数据-caseList
 * 2、caseUtil用来获取列名和列索引的对应关系，以便后续回填case运行结果使用
 */
public class CaseUtil {

    public static List<Case> caseList = Lists.newArrayList();
    public static Map<String,Integer> cellName2CellIndexMap = Maps.newHashMap();
    public static Map<String,Map<String,String>> caseId2resultMap = Maps.newHashMap();
    static {
        ExcelUtil.loadData("",2,Case.class);
    }

}
