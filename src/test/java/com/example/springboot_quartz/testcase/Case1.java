package com.example.springboot_quartz.testcase;

import com.example.springboot_quartz.baseCase.BaseCaseProcess;
import com.example.springboot_quartz.utils.DataProviderUtil;
import org.testng.annotations.DataProvider;

/**
 * created by ${user} on 2019/9/16
 */
public class Case1 extends BaseCaseProcess {


    @DataProvider(name = "data")
    @Override
    public Object[][] dataProvider() {
        String[] collums = {"ApiId","RequestData","Header"};
        Object[][] data = DataProviderUtil.getData("13", collums);

        return data;
    }
}
