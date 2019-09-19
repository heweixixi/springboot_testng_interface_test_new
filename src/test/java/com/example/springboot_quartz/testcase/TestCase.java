package com.example.springboot_quartz.testcase;

import com.example.springboot_quartz.base.CaseBase;
import com.example.springboot_quartz.model.po.MailPo;
import com.example.springboot_quartz.resp.Result;
import com.example.springboot_quartz.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * created by ${user} on 2019/9/16
 */
@Slf4j
public class TestCase extends CaseBase implements Job {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HttpClient httpClient;

    @Autowired
    MailService mailService;

    @Test
    public void test(){
        /*ObjectMapper objectMapper = new ObjectMapper();
        MailPo mailPo = null;
        try {
            mailPo = objectMapper.readValue(mailPoString, MailPo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("test===========================");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MailPo> httpEntity = new HttpEntity<>(mailPo,requestHeaders);
        String url = "http://localhost:8081/mail/sendSimple";
        */
//        ResponseEntity<String> resultResponseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        mailService.sendMailByJavaMail("测试",System.getProperty("user.dir")+ File.separator+"test-output"+File.separator+"html"+File.separator+"index.html");
//        log.info(resultResponseEntity.getBody());
    }

    @DataProvider(name = "data")
    public Object[][] dataProvider(){
        //数据读取excel或者数据库
        Object[][] mailString = {{"{\n" +
                "  \"content\": \"测试\",\n" +
                "  \"subject\": " +
                "}"}};
        return mailString;
    }

//    public static void main(String[] args) {
//        log.info(System.getProperty("user.dir")+ File.separator+"test-output"+File.separator+"html"+"index.html");
//    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
