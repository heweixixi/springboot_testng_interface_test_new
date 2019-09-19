package com.example.springboot_quartz.utils;

import com.example.springboot_quartz.model.case_po.Case;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by ${user} on 2019/9/16
 */
public class DataProviderUtil {

    //通过反射获取数据
    public static Object[][] getData(String apiNum,String[] collums){
        List<Case> caseList = CaseUtil.caseList.stream().filter(aCase -> aCase.getApiId().equals(apiNum)).collect(Collectors.toList());
        Object[][] object = new Object[caseList.size()][collums.length];
        Class clazz = Case.class;
        for (int i = 0; i < caseList.size(); i++) {
            Case aCase = caseList.get(i);
            for (int j = 0; j < collums.length; j++) {
                //获取get方法名
                String methodName = "get"+collums[j];
                try {
                    Method method = clazz.getMethod(methodName);
                    //通过反射掉用case.getMethodName()获取值
                    Object obj = method.invoke(aCase);
                    String s = obj == null ? "" : obj.toString();
                    object[i][j] = s;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }
}
