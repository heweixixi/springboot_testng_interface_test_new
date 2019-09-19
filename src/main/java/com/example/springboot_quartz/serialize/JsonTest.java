package com.example.springboot_quartz.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * created by ${user} on 2019/7/31
 */
public class JsonTest {

    public void test(){
        String json = "{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}},\"expensive\":10}";
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getIncludes().add("store");
        filter.getIncludes().add("book");
        filter.getIncludes().add("category");
        JSONObject jsonObject = JSON.parseObject(json);
        String str = JSON.toJSONString(jsonObject, filter);
        JSONObject jsonObject1 = JSON.parseObject(str);

        System.out.println(str);

    }

    public static void main(String[] args) {
        JsonTest jsonTest = new JsonTest();
        jsonTest.test();
    }

}
