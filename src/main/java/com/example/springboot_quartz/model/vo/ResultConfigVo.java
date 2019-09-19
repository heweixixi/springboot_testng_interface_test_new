package com.example.springboot_quartz.model.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot_quartz.annotataion.ResultConfigVoContraint;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * created by ${user} on 2019/8/2
 */
@Data
@ResultConfigVoContraint
@ApiModel("配置vo")
public class ResultConfigVo {

    /**
     * 原来的结果JSONObject
     */
    private JSONObject originJSONObject;

    /**
     * JSONArray
     */
    private JSONArray originJSONArray;

    /**
     * 新的结果
     */
    private List<String> newResultJson;

    /**
     * 结果标识
     * true 需要序列化
     * false 不需要序列化
     */
    @NotEmpty
    private boolean type;

}
