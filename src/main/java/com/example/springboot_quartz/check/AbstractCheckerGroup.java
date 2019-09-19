package com.example.springboot_quartz.check;

import com.example.springboot_quartz.resp.RESULT_STATUS;
import com.example.springboot_quartz.resp.Result;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by ${user} on 2019/7/25
 */
@Service
@Data
public abstract class AbstractCheckerGroup implements Checker{



    private List<Checker> list = Lists.newArrayList();


    @Override
    public Result check(Object o) {
        Result result = new Result(RESULT_STATUS.SUCCESS.code,RESULT_STATUS.SUCCESS.msg);
        if (CollectionUtils.isEmpty(list)){
            return new Result(RESULT_STATUS.PARAM_ERROR.code,RESULT_STATUS.PARAM_ERROR.msg);
        }
        for (Checker checker : list) {
            result = checker.check(o);
        }
        return result;
    }
}
