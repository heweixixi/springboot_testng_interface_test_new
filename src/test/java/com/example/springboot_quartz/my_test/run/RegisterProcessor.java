package com.example.springboot_quartz.my_test.run;

import com.example.springboot_quartz.my_test.po.Constants;
import org.testng.annotations.DataProvider;
import org.testng.collections.Lists;
import com.example.springboot_quartz.my_test.util.*;

import java.util.List;
import java.util.Map;

public class RegisterProcessor extends UnitFormProcessor{

    @DataProvider
    @Override
    public Object[][] data() {
        List<String> collumnList = Lists.newArrayList("caseId","apiId","requestData","preValidateSql","afterValidateSql");
        Object[][] data = DataProviderUtil.getTestDataByApiId("1", collumnList);
        //查库，从中获取没有注册过的手机号，并将手机号设置到内存变量中，以便后续case使用
        String sql = "select cancat(max(mobilephone)+1) from member";
        Map<String, Object> map = JDBCUtil.query(sql);
        String mobilephone = map.get("mobilephone").toString();
        //数据替换
        //todo
        RestUtil.variableValues.put(Constants.VARIABLE_NORMAL_MOBILEPHONE,mobilephone);
        RestUtil.variableValues.put(Constants.VARIABLE_UN_NORMAL_MOBILEPHONE,"123");
        replaceParam(data);
        return data;
    }

    private void replaceParam(Object[][] data) {
        for (int row = 0; row < data.length; row++) {
            for (int cell = 0; cell < data.length; cell++) {
                if (((String)data[row][cell]).contains(Constants.VARIABLE_NORMAL_MOBILEPHONE)){
                    ((String)data[row][cell]).replace(Constants.VARIABLE_NORMAL_MOBILEPHONE,RestUtil.variableValues.get(Constants.VARIABLE_NORMAL_MOBILEPHONE));
                }
                if (((String)data[row][cell]).contains(Constants.VARIABLE_UN_NORMAL_MOBILEPHONE)){
                    ((String)data[row][cell]).replace(Constants.VARIABLE_UN_NORMAL_MOBILEPHONE,RestUtil.variableValues.get(Constants.VARIABLE_UN_NORMAL_MOBILEPHONE));
                }
            }
        }
    }
}
