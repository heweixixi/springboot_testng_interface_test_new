package com.example.springboot_quartz.my_test.po;

import lombok.Data;
import org.testng.collections.Maps;

import java.util.Map;
@Data
public class CheckerResult {

    private String no;
    private Map<String,Object> collumnName2ResultMap = Maps.newHashMap();
}
