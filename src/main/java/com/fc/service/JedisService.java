package com.fc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Arrays;
import java.util.Set;

@Service
public class JedisService {
    @Autowired
    private JedisPool jedisPool;

    private RedisTemplate<String, Object> redisTemplate;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取value
     * @param key
     * @return
     */
    public String getStringValue(String key){

        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }catch (JedisException  e){
            broken = handleJedisException(e);
        }finally {
            closeResource(jedis, broken);
        }
        return null;
    }


    /**
     * 移除键值
     * @param keys
     */
    public void  remove(String... keys){
        Jedis jedis = null;
        boolean broken = true;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys);
        }catch (JedisException  e){
            broken = handleJedisException(e);
        }finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * 模糊删除
     * @param prex  前缀
     */
    public void removeByPrex(String prex){
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            Set<String> keys = jedis.keys(prex+"*");
            String[] keyArray = new String[keys.size()];
            jedis.del(keys.toArray(keyArray));
        }catch (JedisException  e){
            broken = handleJedisException(e);
        }finally {
            closeResource(jedis, broken);
        }
    }





    /**
     * Handle jedisException, write log and return whether the connection is broken.
     */
    protected boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            logger.error("Redis connection lost.", jedisException);
        } else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
                logger.error("Redis connection  are read-only slave.", jedisException);
            } else {
                // dataException, isBroken=false
                return false;
            }
        } else {
            logger.error("Jedis exception happen.", jedisException);
        }
        return true;
    }
    /**
     * Return jedis connection to the pool, call different return methods depends on the conectionBroken status.
     */
    protected void closeResource(Jedis jedis, boolean conectionBroken) {
        if(jedis != null){
            if (conectionBroken) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }
        }


    }
}
