package com.fc.service;

import com.fc.core.CoreConfig;
import com.fc.domain.GrantingTicket;
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

@Service
public class GrantingTicketService {

    @Autowired
    JedisPool jedisPool;
    @Autowired
    JedisService jedisService;

    @Autowired
    RedisTemplateService redisService;
    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 创建TGT授权凭证
     * @param userId
     * @return
     */
    public String createTGT(String userId){
        if(StringUtils.isBlank(userId)){
            throw new NullPointerException("参数：userId 为空");
        }
        StringBuilder tgt = new StringBuilder();
        tgt.append("TGT")
                .append(NumberUtil.get6RandNum())
                .append("_")
                .append(userId)
                .append("_")
                .append(System.currentTimeMillis());

        return tgt.toString();
    }

    /**
     * 创建TGC用户登录Cookie
     * @param tgt
     * @return
     */
    public String createTGC(String tgt){
        if(StringUtils.isBlank(tgt)){
            throw new NullPointerException("参数：tgt 为空");
        }
        StringBuilder tgc = new StringBuilder();
        tgc.append("TGC")
                .append(MD5Util.stringMD5(tgt))
                .append(MD5Util.stringMD5(String.valueOf(NumberUtil.get6RandNum())));

        return tgc.toString();
    }



    /**
     * 颁发TGT凭证,并返回TGC
     * @param userId
     * @return
     */
    public String awardTGT(String userId){
        String tgt = createTGT(userId);
        String tgc = createTGC(tgt);
        Jedis jedis = null;
        String tgtKey = buildTGTKey(tgc);
        String tgcKey = buildTGCKey(userId);
        //移除旧的TGT
        String oldTgc = jedis.get(tgcKey);
        if(StringUtils.isNotBlank(oldTgc)){
            redisService.delete(buildTGTKey(oldTgc));
        }
        redisService.set(tgtKey, tgt, CoreConfig.TGT_VALID_TIME);
        redisService.set(tgcKey, tgc, CoreConfig.TGT_VALID_TIME);

        return tgc;
    }

    /**
     * 获取tgc
     * @param userId
     * @return
     */
    public String getTGC(String userId){
        return redisService.get(buildTGCKey(userId));
    }

    public GrantingTicket getGrantingTicket(String tgc){
        if(StringUtils.isBlank(tgc)){
            return null;
        }
        String tgt = redisService.get(buildTGTKey(tgc));
        if(StringUtils.isBlank(tgt)){
            return null;
        }
        return resolveTGT(tgt);
    }

    /**
     * 解析TGT授权凭证
     * @param tgt
     * @return
     */
    public GrantingTicket resolveTGT(String tgt){
        String[] values = tgt.split("_");
        if(values == null || values.length != 3){
            throw new RuntimeException("tgt:"+tgt+"  解析失败，格式有误！");
        }
        GrantingTicket grantingTicket = new GrantingTicket();
        grantingTicket.setUserId(values[1]);
        grantingTicket.setLoginTime(Long.valueOf(values[2]));
        grantingTicket.setGrantingTicket(tgt);
        return grantingTicket;
    }

    /**
     * 移除TGT
     * @param tgc
     */
    public void removeTGT(String tgc){
        if(StringUtils.isBlank(tgc)){
            return;
        }
        String key = buildTGTKey(tgc);
        redisService.delete(key);
    }

    /**
     * 移除TGC
     * @param userId
     */
    public void removeTGC(String userId){
        if(StringUtils.isBlank(userId)){
            return;
        }
        String key = buildTGCKey(userId);
        jedisService.remove(key);
    }

    public String buildTGTKey(String tgc){
        return "TGT:" + MD5Util.stringMD5(tgc);
    }
    public String buildTGCKey(String userId){
        return "TGC:" + userId;
    }



}
