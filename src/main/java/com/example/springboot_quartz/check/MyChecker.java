package com.example.springboot_quartz.check;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * created by ${user} on 2019/7/26
 */
@Service
public class MyChecker extends AbstractCheckerGroup{

    @Autowired
    TestChecker testChecker;
    @Autowired
    AnotherChecker anotherChecker;


    @PostConstruct
    public void setList(){
        List<Checker> list = Lists.newArrayList();
        list.add(testChecker);
        list.add(anotherChecker);
        setList(list);
    }
}
