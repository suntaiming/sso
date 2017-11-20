package com.fc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * Spring容器启动时执行的操作
 */
@Service
public class InstantiationProcessor implements ApplicationListener<ContextRefreshedEvent>{
    public InstantiationProcessor() {


    }


    @Autowired
    SubServiceService subServiceService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null){
            //初始化子服务注册信息
            subServiceService.initSubService();
        }
    }
}
