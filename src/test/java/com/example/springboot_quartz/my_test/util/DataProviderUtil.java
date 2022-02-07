package com.example.springboot_quartz.my_test.util;

import com.example.springboot_quartz.model.case_po.Case;
import com.example.springboot_quartz.utils.CaseUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供数据类
 */
public class DataProviderUtil {

    public static Object[][] getTestDataByApiId(String apiId, List<String> collumnList){
        List<Case> caseList = CaseUtil.caseList.stream().filter(caseData -> caseData.getApiId().equals(apiId)).collect(Collectors.toList());
        Object[][] object = new Object[caseList.size()][];
        for (int i = 0; i < caseList.size(); i++) {
            Class aClass = caseList.get(i).getClass();
            for (int j = 0; j < collumnList.size(); j++) {
                try {
                    Method method = aClass.getMethod("get"+collumnList.get(j));
                    object[i][j] = method.invoke(caseList.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

}
