package com.songle.betterjob.handler;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author songle
 * @create 2022-05-22 17:25
 * @descreption jedis处理类
 */


@Data
@Slf4j
public class JedisHandler {


    private String host;
    private int port;
    private int timeout;
    private int database;
    private String serverName;
    private JedisPool jedisPool;


    public JedisHandler(){};

    public JedisHandler(String host, int port, int timeout, int database) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.database = database;
    }

    /**
     * @description 初始化jedis连接信息
     *
     * @param  maxConnectNum
     * @return void
     * @author songle
     * @date    2022/5/22 17:29
     */
    public void init(int maxConnectNum) {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(maxConnectNum);
            this.jedisPool = new JedisPool(jedisPoolConfig,host,port,timeout,null,database);
        }catch (Exception e){
            log.error("init jedispool error,cause:{}",e);
        }
    }


    /**
     * @description 获取redis中的数据
     *
     * @param  key
     * @return void
     * @author songle
     * @date    2022/5/22 17:29
     */
    public String get(String key) {
        try {
            Jedis jedis = jedisPool.getResource();
            return jedis.get(key);
        }catch (Exception e){
            log.error("jedis get info error,cause:{}",e);
        }
        return null;
    }


    /**
     * @description 添加数据
     *
     * @param  key
     * @return void
     * @author songle
     * @date    2022/5/22 17:29
     */
    public String set(String key,String value) {
        try {
            Jedis jedis = jedisPool.getResource();
            return jedis.set(key,value);
        }catch (Exception e){
            log.error("jedis set info error,cause:{}",e);
        }
        return null;
    }


    /**
     * @description 添加数据,原子操作(分布式锁)
     *
     * @param  key
     * @return void
     * @author songle
     * @date    2022/5/22 17:29
     */
    public String set(String key,String value,String nx,String ex,int time) {
        try {
            Jedis jedis = jedisPool.getResource();
            return jedis.set(key,value,nx,ex,time);
        }catch (Exception e){
            log.error("jedis set info error,cause:{}",e);
        }
        return null;
    }


    /**
     * @description 设置过期时间
     *
     * @param  key
     * @return void
     * @author songle
     * @date    2022/5/22 17:29
     */
    public Long expire(String key,int seconds) {
        try {
            Jedis jedis = jedisPool.getResource();
            return jedis.expire(key,seconds);
        }catch (Exception e){
            log.error("jedis expire info error,cause:{}",e);
        }
        return null;
    }


    /**
     * @description 删除数据
     *
     * @param  key
     * @return void
     * @author songle
     * @date    2022/5/22 17:29
     */
    public Long del(String key) {
        try {
            Jedis jedis = jedisPool.getResource();
            return jedis.del(key);
        }catch (Exception e){
            log.error("jedis delete info error,cause:{}",e);
        }
        return null;
    }

}
