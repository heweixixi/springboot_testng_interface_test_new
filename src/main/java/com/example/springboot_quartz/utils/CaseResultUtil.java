package com.example.springboot_quartz.utils;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * created by ${user} on 2019/9/18
 */
public class CaseResultUtil {

    public static Map<String,Map<String,String>> caseIdToCellNameToResultMap = Maps.newHashMap();

    public static void addTestResultInMapping(String caseId,String cellName,String result){
        Map<String, String> map = caseIdToCellNameToResultMap.get(caseId);
        if (map == null){
            map = Maps.newHashMap();
        }
        map.put(cellName,result);
        caseIdToCellNameToResultMap.put(caseId,map);
    }
}
