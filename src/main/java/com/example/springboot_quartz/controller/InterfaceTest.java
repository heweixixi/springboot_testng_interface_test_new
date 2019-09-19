package com.example.springboot_quartz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot_quartz.model.po.DataResult;
import com.example.springboot_quartz.model.po.MailPo;
import com.example.springboot_quartz.resp.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by ${user} on 2019/9/11
 */
@Slf4j
//@RestController
@Api(value = "接口测试",position = 2)
public class InterfaceTest{

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpClient httpClient;

    @BeforeSuite
    public void beforeSuit(){
        log.info("beforeSuit");
    }

    @PostMapping("/post")
    @ApiOperation("接口方法")
    public Result<Object> testMethod1(@RequestBody DataResult dataResult){
        log.info("testMethod1");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("af-bizcode", "tester");
        requestHeaders.add("af-strategy-id", "91");
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataResult> httpEntity = new HttpEntity<>(dataResult,requestHeaders);
        String url = "http://dispatch-sales.aliyun-inc.base/api/realtimeDispatch.json";
        ResponseEntity<String> resultResponseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info(resultResponseEntity.getBody());
        return Result.success(HttpStatus.OK.value(),resultResponseEntity.getBody());
    }

    @Test(dataProvider = "data")
    public Result<Object> testCase(String dataResult1){
        JSONObject jsonObject = JSON.parseObject(dataResult1);
        DataResult dataResult = new DataResult();
        log.info("testMethod1");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("af-bizcode", "tester");
        requestHeaders.add("af-strategy-id", "91");
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataResult> httpEntity = new HttpEntity<>(dataResult,requestHeaders);
        String url = "http://dispatch-sales.aliyun-inc.base/api/realtimeDispatch.json";
        ResponseEntity<String> resultResponseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info(resultResponseEntity.getBody());
        return Result.success(HttpStatus.OK.value(),resultResponseEntity.getBody());
    }

    @DataProvider(name = "data")
    public Object[][] dataProvider(){
        Object[][] data = {{"{\"content\": \"测试\",\"subject\": \"测试\"}"}};
        return data;
    }
//    @Test(groups = {"group2"})
    @PostMapping("/post2")
    @ApiOperation("接口方法2")
    public Result testMethod2(@RequestBody MailPo mailPo){
        log.info("testMethod2");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MailPo> httpEntity = new HttpEntity<>(mailPo,requestHeaders);
        String url = "http://localhost:8081/mail/sendSimple";
        ResponseEntity<String> resultResponseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info(resultResponseEntity.getBody());
        return Result.success(HttpStatus.OK.value(),resultResponseEntity.getBody());
    }


    @AfterSuite(groups = "group2")
    public void afterSuit(){
        log.info("afterSuit");
    }
}
