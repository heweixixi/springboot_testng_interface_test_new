package com.example.springboot_quartz.my_test.util;

import com.example.springboot_quartz.my_test.po.Rest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestUtil {

    public static List<Rest> restList = Lists.newArrayList();
    //存储替换的变量
    public static Map<String,String> variableValues = Maps.newHashMap();

    static {
        ExcelUtil.loadData("",1,Rest.class);
    }

    //根据caseId获取请求的url、type（请求方式）
    public static Rest  getRestByApiId(String apiId){
        List<Rest> restList = RestUtil.restList.stream().filter(rest -> rest.getApiId().equals(apiId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(restList)){
            return restList.get(0);
        }
        return null;
    }
    //get请求
    public static String get(String url, List<NameValuePair> paramList){
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        url+="?"+ URLEncodedUtils.format(paramList,"utf-8");
        try {
            HttpGet httpGet = new HttpGet(new URI(url));
            //todo 需要设置token
            CloseableHttpResponse response = httpClient.execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //post请求
    public static String post(String url,List<NameValuePair> paramList){
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost();
        try {
            post.setURI(new URI(url));
            post.setEntity(new UrlEncodedFormEntity(paramList,"utf-8"));
            //todo 需要设置cookie token
            CloseableHttpResponse response = httpClient.execute(post);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
