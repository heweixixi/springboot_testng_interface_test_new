package com.example.springboot_quartz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot_quartz.model.po.Teacher;
import com.example.springboot_quartz.model.vo.ResultConfigVo;
import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import com.example.springboot_quartz.service.ResultConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * created by ${user} on 2019/8/2
 */
@RestController
@RequestMapping("/result")
@Api(value = "返回结果配置接口",position = 2)
@Slf4j
public class ResultConfigController {

    @Autowired
    ResultConfigService resultConfigService;


    @PostMapping("/config")
    @ApiOperation("配置返回数据")
    public Result resultConfig(@RequestBody  ResultConfigVo resultConfigVo){
        Result result = new Result();
        JSONArray originJSONArray = resultConfigVo.getOriginJSONArray();
        JSONObject originJSONObject = resultConfigVo.getOriginJSONObject();
        originJSONObject = JSON.parseObject(JSON.toJSONString(originJSONObject));

        if (originJSONArray!=null&&originJSONArray.size()>0){
            JSONArray jsonArray = resultConfigService.serializeResultByJSONArray(originJSONArray, resultConfigVo.isType(), resultConfigVo.getNewResultJson());
            result.setData(jsonArray);
        }else if (Objects.nonNull(originJSONObject)){
            JSONObject jsonObject = resultConfigService.serializeResultByJSONObject(originJSONObject, resultConfigVo.isType(), resultConfigVo.getNewResultJson());
            result.setData(jsonObject);
        }
        result.setCode(RESULT_STATUS.SUCCESS.code);
        return result;
    }

    @GetMapping("/getTeacher")
    @ResponseBody
    @ApiOperation("获取老师列表")
    public Integer getTeacher()throws Exception{
        Teacher teacher = resultConfigService.getTeacher();
        Result result = new Result();
        result.setData(null);
        result.setCode(200);
        return 10;
    }
}
