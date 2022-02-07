package com.example.springboot_quartz.my_test.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.collections.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamUtil {

    public static List<NameValuePair> getParam(String requestData){
        List<NameValuePair> nameValuePairList = Lists.newArrayList();
        if (StringUtils.isEmpty(requestData)){
            return nameValuePairList;
        }
        JSONObject jsonObject = JSONObject.parseObject(requestData);
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            nameValuePairList.add(new BasicNameValuePair(entry.getKey(),(String) entry.getValue()));
        }
        return nameValuePairList;
    }
}
