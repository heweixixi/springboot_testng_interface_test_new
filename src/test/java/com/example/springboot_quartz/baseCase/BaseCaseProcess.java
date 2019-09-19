package com.example.springboot_quartz.baseCase;

import com.example.springboot_quartz.base.CaseBase;
import com.example.springboot_quartz.constains.Constains;
import com.example.springboot_quartz.mapper.customer.ResultMapper;
import com.example.springboot_quartz.model.case_po.Rest;
import com.example.springboot_quartz.utils.CaseResultUtil;
import com.example.springboot_quartz.utils.RestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.springboot_quartz.utils.ExcelUtil.writeBatchToExcel;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * created by ${user} on 2019/9/16
 */
@Slf4j
//@Component
public abstract class BaseCaseProcess extends CaseBase {

    @Autowired
    ResultMapper resultMapper;


    @BeforeSuite
    public void beforeSuit(){
        log.info("开始执行接口流程");
    }

    //数据提供，由子类具体提供
    @DataProvider(name = "data")
    public abstract Object[][] dataProvider();

    @Test(dataProvider = "data")
    public void testCase(String apiId,String requestData,String header,String PreValidateSql,String caseId,String afterValidateSql){
        //数据预校验
        if (StringUtils.isNotEmpty(PreValidateSql)){
            //接口请求前的数据处理
            String preDataResult = "PreValidateSql";
            //存储预校验结果
            CaseResultUtil.addTestResultInMapping(caseId,"PreValidateSql",preDataResult);
        }
        //1、获取请求的接口url
        List<Rest> restList = RestUtil.restList.stream().filter(rest -> rest.getApiId().equals(apiId)).collect(Collectors.toList());
        assertNotNull(restList);
        String url = restList.get(0).getUrl();
        //2、获取请求的请求类型
        String type = restList.get(0).getType();
        //3、获取请求参数类型
        String paramType = restList.get(0).getParamType();
        //3、请求接口
        String result;
        if ("post".equals(type)){
            result = RestUtil.postForHttpClient(url, requestData, header,paramType);
        }else {
            result = RestUtil.getForRestTemplate(url,requestData,header);
        }
        log.info(result);
        //存储接口请求的结果
        CaseResultUtil.addTestResultInMapping(caseId,"ActualResponseData",result);

        //请求接口后的数据校验
        if (StringUtils.isNotEmpty(afterValidateSql)){
            //接口请求后的数据处理
            String afterDataResult = "AfterValidateResult";
            CaseResultUtil.addTestResultInMapping(caseId,"AfterValidateResult",afterDataResult);
        }
    }

    /**
     * 替换参数
     * @param data
     */
    public void replaceData(Object[] data){
        for (int i = 0; i < data.length; i++) {
            String requestData = (String) data[i];
            if (requestData.contains(Constains.VARIABLE_NORMAL_MOBILEPHONE)){
                requestData = requestData.replace(Constains.VARIABLE_NORMAL_MOBILEPHONE,RestUtil.mobileMap.get(Constains.VARIABLE_NORMAL_MOBILEPHONE));
            }else if (requestData.contains(Constains.VARIABLE_NON_EXIST_NORMAL_MOBILEPHONE)){
                requestData = requestData.replace(Constains.VARIABLE_NON_EXIST_NORMAL_MOBILEPHONE,RestUtil.mobileMap.get(Constains.VARIABLE_NON_EXIST_NORMAL_MOBILEPHONE));
            }else if (requestData.contains(Constains.VARIABLE_UN_NORMAL_MOBILEPHONE)){
                requestData = requestData.replace(Constains.VARIABLE_UN_NORMAL_MOBILEPHONE,RestUtil.mobileMap.get(Constains.VARIABLE_UN_NORMAL_MOBILEPHONE));
            }
            data[i] = requestData;
        }
    }

    @Parameters(value = {"filepath","sheetNum"})
    @AfterSuite
    public void afterSuit(String filePath,Integer sheetNum){
        log.info("接口流程执行结束");
        //批量写入excel
        writeBatchToExcel(filePath,sheetNum);
    }
}
