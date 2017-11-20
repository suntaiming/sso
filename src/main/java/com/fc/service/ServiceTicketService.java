package com.fc.service;

import com.fc.core.CoreConfig;
import com.fc.util.MD5Util;
import com.fc.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * ST凭证业务类
 */
@Service
public class ServiceTicketService {

    @Autowired
    JedisPool jedisPool;
    @Autowired
    JedisService jedisService;
    @Autowired
    RedisTemplateService redisService;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 颁发ST临时凭证
     * @param userId      用户ID
     * @param serviceId   服务ID
     * @return
     */
    public String awardST(String userId, String serviceId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(serviceId)){
            throw new NullPointerException("参数为空");
        }
        String serviceTicket = createST(userId, serviceId);
        saveST(serviceTicket);
        return serviceTicket;
    }
    /**
     * 生成ST凭证
     * @param userId
     * @param serviceId
     * @return
     */
    private static String createST(String userId, String serviceId){
        StringBuilder serviceTicket = new StringBuilder();
        serviceTicket.append(MD5Util.stringMD5(userId + serviceId +  NumberUtil.get6RandNum() + System.currentTimeMillis()))
                .append("_")
                .append(userId)
                .append("_")
                .append(serviceId);
        return serviceTicket.toString();
    }

    /**
     * 校验ST凭证
     * @param serviceTicket
     * @return
     */
    public boolean verifyST(String serviceTicket){
        if(StringUtils.isBlank(serviceTicket)){
            return false;
        }
        String[] values = serviceTicket.split("_");
        if(values.length != 3){
            return false;
        }

        String originalST = getSTFromRedis(values[1], values[2]);
        if(!serviceTicket.equals(originalST)){
            return false;
        }

        removeST(values[1], values[2]);
        return true;
    }


    /**
     * 保存ST凭证到redis数据库中，并设置有效期为5分钟
     * @param serviceTicket
     */
    public void saveST(String serviceTicket){
        if(StringUtils.isBlank(serviceTicket)){
            return;
        }
        String[] values = serviceTicket.split("_");
        if(values.length != 3){
            logger.error("类：{}--方法：{}-----ST:{} 格式不正确", "ServiceTiceketService", "saveST", serviceTicket);
            throw new RuntimeException("ST:{"+serviceTicket+"} 格式不正确");

        }
        String key = buildSTKey(values[1], values[2]);
        redisService.set(key, serviceTicket, CoreConfig.ST_VALID_TIME);


    }

    /**
     * 从redis库中获取ST凭证
     * @param userId
     * @param serviceId
     * @return
     */
    public String getSTFromRedis(String userId, String serviceId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(serviceId)) {
            return null;
        }
        String key = buildSTKey(userId, serviceId);
        return redisService.get(key);
    }


    /**
     * 移除ST
     * @param userId
     * @param serviceId
     */
    public void removeST(String userId, String serviceId){
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(serviceId)){
            return;
        }
        String key = buildSTKey(userId, serviceId);
        redisService.delete(key);
    }

    public void removeSTByUserId(String userId){
        if(StringUtils.isBlank(userId)){
            return;
        }

        String stKeyPrex = buildSTKeyPrex(userId);
        redisService.deleteByPrex(stKeyPrex);
//        jedisService.removeByPrex(stKeyPrex);
    }



    public String buildSTKey(String userId, String serviceId){
        StringBuilder key = new StringBuilder("ST:");
        key.append(userId)
                .append(":")
                .append(serviceId);

        return key.toString();
    }
    public String buildSTKeyPrex(String userId){
        StringBuilder key = new StringBuilder("ST:");
        key.append(userId)
                .append(":");


        return key.toString();
    }


}
