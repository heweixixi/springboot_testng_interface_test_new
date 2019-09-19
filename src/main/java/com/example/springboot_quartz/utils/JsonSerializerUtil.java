package com.example.springboot_quartz.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created by ${user} on 2019/7/31
 */
public class JsonSerializerUtil {

    /**
     * 新旧json串比较，字段少的剔除不再解析
     * @param originJson
     * @param newJson
     */
    public void serializeByJson(JSONObject originJson,JSONObject newJson){


    }

    /**
     * 直接通过给定的key来剔除不需要解析的key
     * @param nameList
     */
    public static List<JSONObject> serializeByField(){
        String json = "[{\"code\":\"200\",\"data\":[{    \"ownerType\":1,    \"gmtModified\":1564640140000,    \"name\":\"束文华加策略组\",    \"lastModifiedEmpid\":\"WB459715\",    \"strategyDetailInfoList\":[{        \"gmtModified\":1564640140000,        \"value2\":\"\",        \"value1\":\"束文华\",        \"strategyGroupId\":2790,        \"gmtCreate\":1564640140000,        \"compareAttributeType\":\"STRING\",        \"createdEmpid\":\"WB459715\",        \"compareAttributeCode\":\"devata_tag__alibaba_cloud_manager\",        \"lastModifiedEmpid\":\"WB459715\",        \"id\":3816,        \"operatorType\":\"contain\",        \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyDetailInfoDTO\",        \"isDel\":0    },    {        \"gmtModified\":1564640140000,        \"value2\":\"\",        \"value1\":\"true\",        \"strategyGroupId\":2790,        \"gmtCreate\":1564640140000,        \"compareAttributeType\":\"BOOLEAN\",        \"createdEmpid\":\"WB459715\",        \"compareAttributeCode\":\"devata_tag__is_public\",        \"lastModifiedEmpid\":\"WB459715\",        \"id\":3817,        \"operatorType\":\"equals\",        \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyDetailInfoDTO\",        \"isDel\":0    }],    \"id\":2790,    \"ownerId\":727,    \"gmtCreate\":1564640140000,    \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyGroupDTO\",    \"isDel\":0,    \"createdEmpid\":\"WB459715\"}],\"success\":true,\"count\":1,\"message\":\"successful\",\"class\":\"com.aliyun.sales.lead.shared.service.result.ListResult\"}]";
//        JSONObject jsonObj = JSON.parseObject(json);
        JSONArray jsonArray = JSON.parseArray(json);
        LevelPropertyPreFilter propertyPreFilter = new LevelPropertyPreFilter();
//        propertyPreFilter.addExcludes("data.strategyDetailInfoList.compareAttributeCode");
//        propertyPreFilter.addExcludes("class");
//        propertyPreFilter.addIncludes("data");
        return getJSONObject(jsonArray);


    }

    private static List<JSONObject> getJSONObject(JSONArray jsonArray){
        List<JSONObject> list = Lists.newArrayList();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.getJSONObject(i));
        }
//        for (Object o : jsonArray) {
//            try {
//                JSONObject jsonObject = (JSONObject) o;
//            }catch (Exception e){
//                JSONArray innerJSONArray = (JSONArray)o;
//                getJSONObject(innerJSONArray);
//            }
//        }
        return list;
    }


    public static boolean isBasicType(Object param){
        if (param instanceof Integer || param instanceof String
                || param instanceof Double ||param instanceof Float
                || param instanceof Long ||param instanceof Boolean
                ||param instanceof Date) {
            return true;
        }else {
            return false;
        }
    }

//    private static boolean isJSONArray(Object param){
//
//    }
//    public static void main(String[] args) {
////        JsonSerializerUtil.serializeByField();
//        String json = "[{\"code\":\"200\",\"data\":[{    \"ownerType\":1,    \"gmtModified\":1564640140000,    \"name\":\"束文华加策略组\",    \"lastModifiedEmpid\":\"WB459715\",    \"strategyDetailInfoList\":[{        \"gmtModified\":1564640140000,        \"value2\":\"\",        \"value1\":\"束文华\",        \"strategyGroupId\":2790,        \"gmtCreate\":1564640140000,        \"compareAttributeType\":\"STRING\",        \"createdEmpid\":\"WB459715\",        \"compareAttributeCode\":\"devata_tag__alibaba_cloud_manager\",        \"lastModifiedEmpid\":\"WB459715\",        \"id\":3816,        \"operatorType\":\"contain\",        \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyDetailInfoDTO\",        \"isDel\":0    },    {        \"gmtModified\":1564640140000,        \"value2\":\"\",        \"value1\":\"true\",        \"strategyGroupId\":2790,        \"gmtCreate\":1564640140000,        \"compareAttributeType\":\"BOOLEAN\",        \"createdEmpid\":\"WB459715\",        \"compareAttributeCode\":\"devata_tag__is_public\",        \"lastModifiedEmpid\":\"WB459715\",        \"id\":3817,        \"operatorType\":\"equals\",        \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyDetailInfoDTO\",        \"isDel\":0    }],    \"id\":2790,    \"ownerId\":727,    \"gmtCreate\":1564640140000,    \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyGroupDTO\",    \"isDel\":0,    \"createdEmpid\":\"WB459715\"}],\"success\":true,\"count\":1,\"message\":\"successful\",\"class\":\"com.aliyun.sales.lead.shared.service.result.ListResult\"}]";
//        Object parse = 1;
//        if (parse instanceof JSONObject){
//            System.out.println("JSONObject");
//        }else if (parse instanceof JSONArray){
//            System.out.println("JSONArray");
//        }else if (isBasicType(parse)){
//            System.out.println("basic");
//        }else {
//            System.out.println("nothing");
//        }
//
//    }

    private static void circle(Object o,LevelPropertyPreFilter propertyPreFilter,StringBuffer buffer,Integer floor,List<Integer> floorList,List<String> propertys){
        if (isBasicType(o)){
            System.out.println(o);
        }else if (o instanceof JSONObject){
            JSONObject object = (JSONObject)o;
            floor++;
            for (String key : object.keySet()) {
                if (propertys.size()>=floor&& !org.apache.commons.lang.StringUtils.equals(propertys.get(floor-1),key)){
                    continue;
                }
                if (!CollectionUtils.isEmpty(floorList)&&floorList.get(0)>=floor && buffer.length()>0&&buffer.toString().contains(".")){
                    for (int i=0; i<(floorList.get(0)-floor+1);i++){
                        buffer = new StringBuffer(buffer.toString().substring(0, buffer.toString().lastIndexOf(".")));
                    }
                }
                floorList.clear();
                floorList.add(floor);
                buffer.append(".").append(key);
                propertyPreFilter.addIncludes(buffer.toString());
                circle(object.get(key),propertyPreFilter,buffer,floor,floorList,propertys);
            }
        }else if (o instanceof JSONArray){
            JSONArray jsonArray = (JSONArray)o;
            for (Object o1 : jsonArray) {
                circle(o1,propertyPreFilter,buffer,floor,floorList,propertys);
            }
        }
    }

    private static void setPropertyFilter(List<String> serializeFields,JSONObject jsonObject, LevelPropertyPreFilter propertyPreFilter){
        List<Integer> floorList = Lists.newArrayList();
        if (org.apache.commons.collections.CollectionUtils.isEmpty(serializeFields)){
            return;
        }
        for (String  serializeField: serializeFields) {
            StringBuffer buffer = new StringBuffer();
            if (serializeField.contains(".")){
//            String[] serializeFields = serializeField.split("\\.");
                List<String> list = Lists.newArrayList(serializeField.split("\\."));
//                for (int i = 0; i < list.size(); i++) {
//                    if (i==0){
                        buffer.append(list.get(0));
//                    }else {
//                        buffer.append(".").append(list.get(i));
//                    }
                    propertyPreFilter.addIncludes(buffer.toString());
                    Object o = jsonObject.get(list.get(0));
                    circle(o,propertyPreFilter,buffer,1,floorList,list);
//                }
            }else {
                propertyPreFilter.addIncludes(serializeField);
                Object o = jsonObject.get(serializeField);
                buffer.append(serializeField);
                circle(o,propertyPreFilter,buffer,1,floorList,Lists.newArrayList());
            }
        }
    }

    public static void main(String[] args) {
        String json = "{\"code\":\"200\",\"data\":[{    \"ownerType\":1,    \"gmtModified\":1564640140000,    \"name\":\"束文华加策略组\",    \"lastModifiedEmpid\":\"WB459715\",    \"strategyDetailInfoList\":[{        \"gmtModified\":1564640140000,        \"value2\":\"\",        \"value1\":\"束文华\",        \"strategyGroupId\":2790,        \"gmtCreate\":1564640140000,        \"compareAttributeType\":\"STRING\",        \"createdEmpid\":\"WB459715\",        \"compareAttributeCode\":\"devata_tag__alibaba_cloud_manager\",        \"lastModifiedEmpid\":\"WB459715\",        \"id\":3816,        \"operatorType\":\"contain\",        \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyDetailInfoDTO\",        \"isDel\":0    },    {        \"gmtModified\":1564640140000,        \"value2\":\"\",        \"value1\":\"true\",        \"strategyGroupId\":2790,        \"gmtCreate\":1564640140000,        \"compareAttributeType\":\"BOOLEAN\",        \"createdEmpid\":\"WB459715\",        \"compareAttributeCode\":\"devata_tag__is_public\",        \"lastModifiedEmpid\":\"WB459715\",        \"id\":3817,        \"operatorType\":\"equals\",        \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyDetailInfoDTO\",        \"isDel\":0    }],    \"id\":2790,    \"ownerId\":727,    \"gmtCreate\":1564640140000,    \"class\":\"com.aliyun.sales.lead.shared.service.model.StrategyGroupDTO\",    \"isDel\":0,    \"createdEmpid\":\"WB459715\"}],\"success\":true,\"count\":1,\"message\":\"successful\",\"class\":\"com.aliyun.sales.lead.shared.service.result.ListResult\"}";
        JSONObject jsonObj = JSON.parseObject(json);
        Map<String,Integer> map = Maps.newHashMap();
        List<String> names = Lists.newArrayList("data","data.lastModifiedEmpid");
        for (String name : names) {
            if (name.contains(".")){
                String[] split = name.split("\\.");
                map.put(name,split.length);
            }else {
                map.put(name,1);
            }
        }
        List<Map.Entry> list = Lists.newArrayList(map.entrySet());
        Collections.sort(list,(o1,o2)->(Integer.valueOf(o2.getValue().toString())-Integer.valueOf(o1.getValue().toString())));
        String maxName = (String) list.get(0).getKey();
        List<String> nameList = Lists.newArrayList(names).stream().filter(name -> !maxName.contains(name)).collect(Collectors.toList());
        nameList.add(maxName);


//        JSONArray jsonArray = JSON.parseArray(json);
        LevelPropertyPreFilter propertyPreFilter = new LevelPropertyPreFilter();
//        propertyPreFilter.addExcludes("data.strategyDetailInfoList.compareAttributeCode");
//        propertyPreFilter.addExcludes("class");
//        propertyPreFilter.addIncludes("data");
//        List<JSONObject> jsonObjects = JsonSerializerUtil.getJSONObject(jsonArray);
//        JSONObject jsonObject = jsonObjects.get(0);
//        JSONObject jsonObject = JSON.parseObject(json);
//        String[] split = name.split("\\.");
//        StringBuffer buffer = new StringBuffer();
//        if (!CollectionUtils.isEmpty(jsonObjects)){
//            for (JSONObject jsonObj : jsonObjects) {
//                setPropertyFilter(nameList,jsonObj,propertyPreFilter);
//            }
//        }
        setPropertyFilter(nameList,jsonObj,propertyPreFilter);

        String s = JSON.toJSONString(jsonObj, propertyPreFilter);
        System.out.println(JSON.parseObject(s));



    }



    }

