package com.example.springboot_quartz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import com.example.springboot_quartz.model.po.Student;
import com.example.springboot_quartz.model.po.Subject;
import com.example.springboot_quartz.model.po.Teacher;
import com.example.springboot_quartz.resp.Result;
import com.example.springboot_quartz.utils.JsonSerializerUtil;
import com.example.springboot_quartz.utils.LevelPropertyPreFilter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by ${user} on 2019/8/2
 */
@Service
public class ResultConfigService {

    /**
     *
     * @param object
     * @param type true 需要序列化 false不需要序列化
     * @return
     */
    public JSONObject serializeResultByJSONObject(JSONObject object, boolean type, List<String> serializeField){
        LevelPropertyPreFilter propertyPreFilter = new LevelPropertyPreFilter();
        if (type){
            List<String> newJsonList = filterNewJson(serializeField);
            setPropertyFilter(newJsonList,object,propertyPreFilter);
        }else {
            for (String excluedName : serializeField) {
                propertyPreFilter.addExcludes(excluedName);
            }
        }
        String newJson = JSON.toJSONString(object, propertyPreFilter);
        return JSON.parseObject(newJson);
    }

    public JSONArray serializeResultByJSONArray(JSONArray jsonArray, boolean type, List<String> serializeField){
        LevelPropertyPreFilter propertyPreFilter = new LevelPropertyPreFilter();
        if (type){
            List<String> newJsonList = filterNewJson(serializeField);
            List<JSONObject> jsonObjects = getJSONObject(jsonArray);
            if (!CollectionUtils.isEmpty(jsonObjects)){
                for (JSONObject jsonObj : jsonObjects) {
                    setPropertyFilter(newJsonList,jsonObj,propertyPreFilter);
                }
            }
        }else {
            for (String excluedName : serializeField) {
                propertyPreFilter.addExcludes(excluedName);
            }
        }
        String newJson = JSON.toJSONString(jsonArray, propertyPreFilter);
        return JSON.parseArray(newJson);
    }


    private List<String> filterNewJson(List<String> newJson){
        Map<String,Integer> map = Maps.newHashMap();
        for (String name : newJson) {
            if (name.contains(".")){
                String[] split = name.split("\\.");
                map.put(name,split.length);
            }else {
                map.put(name,1);
            }
        }
        List<Map.Entry> list = Lists.newArrayList(map.entrySet());
        Collections.sort(list,(o1, o2)->(Integer.valueOf(o2.getValue().toString())-Integer.valueOf(o1.getValue().toString())));
        String maxName = (String) list.get(0).getKey();
        List<String> nameList = newJson.stream().filter(name -> !maxName.contains(name)).collect(Collectors.toList());
        nameList.add(maxName);
        return nameList;
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
    private void setPropertyFilter(List<String> serializeFields,JSONObject jsonObject, LevelPropertyPreFilter propertyPreFilter){
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



    public static void main(String[] args) {
       /* Map<Integer,String> map = Maps.newHashMap();
        String name = "name.is.aris";
        String[] names = name.split("\\.");
        StringBuffer buffer = new StringBuffer();
        List<StringBuffer> list = Lists.newArrayList();
        for (int i = 0; i < names.length; i++) {

//            if (i==0){
//                map.put(i,name.substring(0,name.indexOf(".")));
//            }else {
//                map.put(i,name.substring(0,name.indexOf(".",name.indexOf(".")+1)));
//            }
            if (i==0){
                buffer.append(names[i]);
                System.out.println(buffer.toString());
            }else {
                buffer.append(".").append(names[i]);
                System.out.println(buffer.toString());
            }
            list.add(buffer);
        }*/
//        list.forEach(stringBuffer-> System.out.println(stringBuffer.toString()));
//        for (String value : map.values()) {
//            System.out.println(value);
//        }
        String number = "1234.56";
        System.out.println(Double.valueOf(number));
        Double total = 0.0;
//        for (int i = 0; i < number.length(); i++) {
            String[] split = number.split("\\.");
            for (int i1 = 0; i1 < split.length; i1++) {
                if (i1==0){
                    Integer number1 = new Integer(split[i1]);
                    total +=number1;
                }
                if (i1>0){
                    Double number2 = new Double(split[i1]) / 100;
                    total += number2;
                }
            }
//        }
        if (total instanceof Double){
            System.out.println(total);
        }

    }

    public Teacher getTeacher()throws Exception{
        Subject subject = new Subject("语文","数学");
        List<Subject> subjectList = Lists.newArrayList();
        subjectList.add(subject);
        Student student = new Student(10,"张三",subjectList);
        List<Student> studentList = Lists.newArrayList();
        studentList.add(student);
        return new Teacher(20,"老师",studentList);
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

}
