package com.fc.service;

import com.fc.core.SubServiceTemplate;
import com.fc.mapper.SubServiceMapper;
import com.fc.model.SubService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubServiceService {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SubServiceMapper subServiceMapper;
    public SubService getService(String serviceId){
        if(StringUtils.isBlank(serviceId)){
            return null;
        }

        return subServiceMapper.selectByServiceId(serviceId);
    }

    public List<SubService> findAll(){
        return subServiceMapper.selectAll();
    }

    public void updateLogoutAll(String serviceId, int logoutAll){
        if(StringUtils.isBlank(serviceId)){
            throw new NullPointerException("ServiceService--updateLogoutAll方法参数为空");
        }

        subServiceMapper.updateLogoutAll(serviceId, logoutAll);
    }

    public void insertSubService(SubService subService){
        subServiceMapper.insertSubService(subService);
    }

    /**
     * 初始化子服务信息到mysql
     */
    public void initSubService(){
        logger.info("初始化原始子服务注册信息...");
        System.out.println("初始化原始子服务注册信息...");
        subServiceMapper.deleteAll();
        for (SubService subService : SubServiceTemplate.subServices){
            insertSubService(subService);
        }
    }

    /**
     * 将服务注册信息初始化到Redis中
     */
    public void initServiceToRedis(){

    }
}
