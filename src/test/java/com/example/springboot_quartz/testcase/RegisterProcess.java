package com.example.springboot_quartz.testcase;

import com.example.springboot_quartz.baseCase.BaseCaseProcess;
import com.example.springboot_quartz.constains.Constains;
import com.example.springboot_quartz.mapper.customer.ResultMapper;
import com.example.springboot_quartz.model.po.Student;
import com.example.springboot_quartz.utils.CaseUtil;
import com.example.springboot_quartz.utils.DataProviderUtil;
import com.example.springboot_quartz.utils.ExcelUtil;
import com.example.springboot_quartz.utils.RestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.annotations.DataProvider;

/**
 * created by ${user} on 2019/9/17
 */

/**
 * 注册流程
 */
@Component
public class RegisterProcess extends BaseCaseProcess {

    @Autowired
    ResultMapper resultMapper;


    @DataProvider(name = "data")
    @Override
    public Object[][] dataProvider() {
        //通过数据提供工具类获取excel数据
        String[] collums = {"ApiId","RequestData","Header","PreValidateSql","CaseId","AfterValidateSql"};
        Object[][] data = DataProviderUtil.getData("15", collums);
        String mobile = resultMapper.getMaxAgeStudent();
        RestUtil.mobileMap.put(Constains.VARIABLE_NORMAL_MOBILEPHONE,mobile);
        RestUtil.mobileMap.put(Constains.VARIABLE_UN_NORMAL_MOBILEPHONE,"111");
        for (int i = 0; i < data.length; i++) {
            replaceData(data[i]);
        }
        return data;
    }
}
