package com.example.springboot_quartz.check;

import com.example.springboot_quartz.resp.Result;
import org.springframework.stereotype.Service;

/**
 * created by ${user} on 2019/7/25
 */
public interface Checker {

    Result check(Object o);
}
