package com.example.springboot_quartz.annotataion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot_quartz.model.vo.ResultConfigVo;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * created by ${user} on 2019/8/2
 */
@Documented
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ResultConfigVoContraint.ResultConfigValidator.class)
public @interface ResultConfigVoContraint {
    String message() default "参数格式错误";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class ResultConfigValidator implements ConstraintValidator<ResultConfigVoContraint, ResultConfigVo>{

        @Override
        public void initialize(ResultConfigVoContraint constraintAnnotation) {

        }

        @Override
        public boolean isValid(ResultConfigVo resultConfigVo, ConstraintValidatorContext constraintValidatorContext) {
            Object originResultJson ="";
            try {
                JSON.parseObject(originResultJson.toString());
            } catch (Exception e) {
                try {
                    JSON.parseArray(originResultJson.toString());
                } catch (Exception e1) {
                    return false;
                }
                return false;
            }
            return true;
        }
    }
}
