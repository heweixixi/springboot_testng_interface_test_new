package com.example.springboot_quartz.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot_quartz.constains.Constains;
import com.example.springboot_quartz.model.case_po.Rest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.example.springboot_quartz.utils.HttpClientConfigUtil.getHttpClient;


/**
 * created by ${user} on 2019/9/16
 */
@Slf4j
//@Component
public class RestUtil {

    public static List<Rest> restList = new ArrayList<>();
    //存储cookie使用
    private static Map<String, String> map = new HashMap<String, String>();

    public static Map<String,String> mobileMap = Maps.newHashMap();

    //初始化加载excel数据，填充restList数据
    static {
        log.info("========================RestUtil static========================");
        ExcelUtil.loadBeans("src/test/resources/rest_info.xlsx",0,Rest.class);
    }

    public static List<NameValuePair> getNameValuePair(String requestData){
        Gson gson = new Gson();
        if (StringUtils.isEmpty(requestData)){
            return new ArrayList<NameValuePair>();
        }
        Map<String,String> map = gson.fromJson(requestData, Map.class);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            BasicNameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(),entry.getValue());
            params.add(nameValuePair);
        }
        return params;
    }

    public static Map<String,Object> getDataMap(String requestData){
        Gson gson = new Gson();
        Map<String,Object> map = gson.fromJson(requestData, Map.class);
        return map;
    }

    public static Map<String,String> getHeader(String header){
        Gson gson = new Gson();
        if (StringUtils.isEmpty(header)){
            return Maps.newHashMap();
        }
        Map<String,String> map = gson.fromJson(header, Map.class);
        return map;
    }

    /**
     * 1、可以使用httpclient方式post请求接口
     * @param url
     * @param requestData
     * @param headers
     * @return
     */
    public static String postForHttpClient(String url,String requestData,String headers,String paramType){
        try {
            //决定接口提交方式
            HttpPost post = new HttpPost();
            post.setURI(new URI(url));
            Map<String, String> paramMap = getHeader(headers);
            if (paramMap!=null&&paramMap.size()!=0){
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    post.addHeader(entry.getKey(),entry.getValue());
                }
            }
            post.addHeader("Cookie",map.get("JSESSIONID"));
            if (StringUtils.isEmpty(paramType)){
                //返回错误信息
                return "paramType不能为空";
            }else if (Constains.OBJECT.equals(paramType)){
                post.addHeader("content-type","application/json");
                //将参数设置到post请求中
                StringEntity entity = new StringEntity(requestData, "utf-8");
                post.setEntity(entity);
            }else if (Constains.SIMPLE.equals(paramType)){
                //带有@RequestBody注解的简单参数，使用json串的值直接请求
                post.addHeader("content-type","application/json");
                JSONObject jsonObject = JSON.parseObject(requestData);
                List<String> paramList = Lists.newArrayList();
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    paramList.add(entry.getValue().toString());
                }
                StringEntity entity = new StringEntity(paramList.get(0), "utf-8");
                post.setEntity(entity);
            }else if (Constains.NO_ANNOTATIONS_SIMPLE.equals(paramType)){
                //不带有@RequestBody注解的简单参数，使用url拼接的方式
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                if (StringUtils.isNotEmpty(requestData)){
                    Map<String,Object> map =  JSON.parseObject(requestData, Map.class);
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        //构建查询参数
                        builder.queryParam(entry.getKey(),entry.getValue());
                    }
                }
                //拼接好参数后的url
                String paramUrl = builder.build().toString();
                post.setURI(new URI(paramUrl));
            }else if (Constains.NO_ANNOTATIONS_SIMPLE_AND_OBJECT.equals(paramType)){
                //不带有@RequestBody注解的简单参数，使用url拼接的方式
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
                JSONArray jsonArray = JSONArray.parseArray(paramType);
                for (int i = 0; i < jsonArray.size(); i++) {
                    Map<String,Object> map = JSON.parseObject(jsonArray.getString(i), Map.class);
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        //构建查询参数
                        builder.queryParam(entry.getKey(),entry.getValue());
                    }
                }
                //拼接好参数后的url
                String paramUrl = builder.build().toString();
                post.setURI(new URI(paramUrl));
            }
            //发动请求
            CloseableHttpClient httpClient =HttpClients.createDefault();
            CloseableHttpResponse response =httpClient.execute(post);
            return response.getEntity().getContent().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 2、使用resttemplate方式post请求
     * @param url
     * @param requestData
     * @param headers
     * @return
     */
    public static String postForRestTemplate(String url,String requestData,String headers,String paramType){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, String> paramMap = getHeader(headers);
        if (paramMap!=null&&paramMap.size()!=0){
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                httpHeaders.add(entry.getKey(),entry.getValue());
            }
        }
        // 可以使用 HashMap
        Map<String, Object> params= new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(requestData);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            params.put(entry.getKey(),entry.getValue());
        }
        log.warn(JSONObject.toJSONString(params));
        // 构造 HttpEntity 请求参数, JSON 方式提交只能使用 JSON 字符串
        HttpEntity<String> httpEntity = new HttpEntity<>(JSONObject.toJSONString(params), httpHeaders);
        // 发起请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        return responseEntity.getBody();
    }

    /**
     * 使用resttemplate进行get请求
     * @param url
     * @param requestData
     * @param headers
     * @return
     */
    public static String getForRestTemplate(String url,String requestData,String headers){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie",map.get("JSESSIONID"));
        Map<String,String> headerMap = getHeader(headers);
        HttpEntity<String> httpEntity = new HttpEntity<String>(null,httpHeaders);
        if (headerMap!=null&&headerMap.size()!=0){
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpHeaders.add(entry.getKey(),entry.getValue());
            }
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (StringUtils.isNotEmpty(requestData)){
            Map<String,Object> paramMap =  JSON.parseObject(requestData, Map.class);
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                //构建查询参数
                builder.queryParam(entry.getKey(),entry.getValue());
            }
        }
        //拼接好参数后的url
        String paramUrl = builder.build().toString();
        ResponseEntity<String> stringResponseEntity = restTemplate.exchange(paramUrl, HttpMethod.GET, httpEntity,String.class);
        return stringResponseEntity.getBody();
    }

    /**
     * 使用httpclient进行get请求
     * @param url
     * @param requestData
     * @param headers
     * @return
     */
    public static String getForHttpClient(String url,String requestData,String headers){
        HttpClient httpClient = getHttpClient();
        HttpGet httpGet = new HttpGet();
        try {
            httpGet.addHeader("Cookie",map.get("JSESSIONID"));
            Map<String, String> headerMap = getHeader(headers);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpGet.addHeader(entry.getKey(),entry.getValue());
            }

            List<NameValuePair> nameValuePair = getNameValuePair(requestData);
            String format = URLEncodedUtils.format(nameValuePair, "utf-8");
            String newUrl =url+"?"+format;
            httpGet.setURI(new URI(newUrl));
            HttpResponse execute = httpClient.execute(httpGet);
            return execute.getEntity().toString();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static void addJsessionInMap(HttpResponse response) {
        if (response.getFirstHeader("Set-Cookie") != null) {
            String setCookieValue = response.getFirstHeader("Set-Cookie").getValue();
            if (setCookieValue != null && setCookieValue.trim().length()>0) {
                int index = setCookieValue.indexOf("JSESSIONID");
                if (index != -1) {
                    setCookieValue = setCookieValue.substring(setCookieValue.indexOf("JSESSIONID"));
                    int index2 = setCookieValue.indexOf(";");
                    if (index2 != -1) {
                        String jsessionId = setCookieValue.substring(0, setCookieValue.indexOf(";"));
                        map.put("JSESSIONID", jsessionId);

                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        /*RestTemplate restTemplate = new RestTemplate();
        String param = "{\"id\":\"310\"}";
        Gson gson = new Gson();
        ObjectMapper objectMapper = new ObjectMapper();
        String newParam = gson.toJson(param);
        try {
            newParam= objectMapper.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        log.info(newParam);
        try {
            HttpEntity<String> httpEntity = new HttpEntity<>(param, httpHeaders);
//            restTemplate.postForEntity("http://localhost:8081/get",httpEntity,String.class);
            Result result =restTemplate.getForObject("http://localhost:8081/get?id=323", Result.class);
            log.info(result.getData().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        JSONArray jsonArray = JSONArray.parseArray("[{\"id\":\"328\"},{\"id\":\"11\",\"name\":\"ad\"}]");
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<String,Object> map = JSON.parseObject(jsonArray.getString(i), Map.class);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }

        }

    }


}
