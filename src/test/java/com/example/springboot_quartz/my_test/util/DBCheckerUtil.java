package com.example.springboot_quartz.my_test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot_quartz.my_test.po.CheckerResult;
import com.example.springboot_quartz.my_test.po.DBChechker;
import org.testng.collections.Lists;

import java.util.List;
import java.util.Map;

public class DBCheckerUtil {

    public static String check(String ValidateSql){
        List<CheckerResult> resultList = Lists.newArrayList();
        List<DBChechker> dbChechkers = JSONObject.parseArray(ValidateSql, DBChechker.class);
        for (DBChechker chechker : dbChechkers) {
            CheckerResult checkerResult = new CheckerResult();
            String no = chechker.getNo();
            String sql = chechker.getSql();
            Map<String, Object> map = JDBCUtil.query(sql);
            checkerResult.setNo(no);
            checkerResult.setCollumnName2ResultMap(map);
            resultList.add(checkerResult);
        }
        String dbCheckResult = JSON.toJSONString(resultList);
        return dbCheckResult;
    }
}
