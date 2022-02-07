package com.example.springboot_quartz.my_test.run;

import com.example.springboot_quartz.model.case_po.DBChecker;
import com.example.springboot_quartz.my_test.po.Rest;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.testng.annotations.*;
import com.example.springboot_quartz.my_test.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class UnitFormProcessor {

    @BeforeSuite
    public void beforeSuit(){
      log.info("开始执行case");
    }
    @Test(dataProvider = "data")
    public void run_suit(String caseId,String apiId,String requestData,String preValidateSql,String afterValidateSql){
        //1、执行preValidateSql校验
        if (StringUtils.isNotEmpty(preValidateSql)){
            String befordValidateSqlRestul = "";
            //todo 需要进行执行前sql校验
            if (StringUtils.isNotEmpty(preValidateSql)){
                befordValidateSqlRestul = DBCheckerUtil.check(preValidateSql);
            }
            Map<String,String> cellName2beforeValidateSqlResultMap = Maps.newHashMap();
            cellName2beforeValidateSqlResultMap.put("AfterValidateResult",befordValidateSqlRestul);
            CaseUtil.caseId2resultMap.put(caseId,cellName2beforeValidateSqlResultMap);
        }
        //2、执行requestData请求
        /**
         * 需要获取请求的url和请求类型
         */
        Rest rest = RestUtil.getRestByApiId(apiId);
        String url = rest.getUrl();
        String type = rest.getType();
        String restResult = "";
        List<NameValuePair> param = ParamUtil.getParam(requestData);
        if ("post".equals(type)){
            restResult = RestUtil.post(url,param);
        }else if ("get".equals(type)){
            restResult = RestUtil.get(url,param);
        }
        Map<String,String> cellName2RestResultMap = Maps.newHashMap();
        cellName2RestResultMap.put("ActualResponseData",restResult);
        CaseUtil.caseId2resultMap.put(caseId,cellName2RestResultMap);

        //3、执行afterValidateSql校验
        if (StringUtils.isNotEmpty(afterValidateSql)){
            String afterValidateSqlRestul = "";
            //todo 需要进行执行后sql校验
            if (StringUtils.isNotEmpty(afterValidateSql)){
                afterValidateSqlRestul = DBCheckerUtil.check(afterValidateSql);
            }
            Map<String,String> cellName2afterValidateSqlResultMap = Maps.newHashMap();
            cellName2afterValidateSqlResultMap.put("AfterValidateResult",afterValidateSqlRestul);
            CaseUtil.caseId2resultMap.put(caseId,cellName2afterValidateSqlResultMap);
        }
    }

    @DataProvider(name = "data")
    public abstract Object[][] data();


    @AfterSuite
    @Parameters(value = {"filepath","sheetNum"})
    public void afterSuit(String filepath,String sheetNum){
        /**
         * 1、将运行结果回填excel
         * 2、将测试报告发送邮件通知
         */
        ExcelUtil.writeResultToExcel(filepath,sheetNum);
    }
}
