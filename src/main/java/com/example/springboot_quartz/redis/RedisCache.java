package com.example.springboot_quartz.redis;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;

public class RedisCache implements Cache{

    private final static String IP = "127.0.0.1";
    private final static int PORT = 6379;
    private final static String PASSWORD = "xixi";
    private static JedisPool pool;
    private String namespace = "default";
    private static int maxTotal = 3000;
    private static int maxIdle = 10;
    private static int maxWaitMillis = 60000;//ms

    private static final int DEFAULT_CONN_TIME_OUT = 3000;//ms


   static  {
        JedisPoolConfig config = new JedisPoolConfig();
        // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(maxTotal);
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(maxIdle);
        //最小空闲连接数, 默认0
        config.setMinIdle(10);
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted), 如果超时就抛异常, 小于零:阻塞不确定的时间, 默认 - 1
        config.setMaxWaitMillis(maxWaitMillis);
        // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        config.setTestOnReturn(Boolean.FALSE);
        //在空闲时检查有效性, 默认false
        config.setTestWhileIdle(true);
        //逐出扫描的时间间隔(毫秒) 如果为负数, 则不运行逐出线程, 默认 - 1
        config.setTimeBetweenEvictionRunsMillis(60000);
        //对象空闲多久后逐出, 当空闲时间 > 该值 且 空闲连接>最大空闲数 时直接逐出, 不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
        config.setSoftMinEvictableIdleTimeMillis(1800000);
        //每次逐出检查时 逐出的最大数目 如果为负数就是: idleObjects.size / abs(n), 默认3
        config.setNumTestsPerEvictionRun(3);

        pool = new JedisPool(config,IP,PORT,DEFAULT_CONN_TIME_OUT,PASSWORD);
    }


    public Jedis getInstane(){
       return pool.getResource();
    }

    @Override
    public <T extends Serializable> T get(String key) {
        return get(key,namespace);
    }

    @Override
    public <T extends Serializable> T get(String key, String nameSpace) {
        if (StringUtils.isEmpty(key)){
            return null;
        }
        key = genKey(key,namespace);
        Jedis jedis = getInstane();
        return null;
    }

    @Override
    public <T extends Serializable> T put(String key, T obj, int expireSeconds) {
        return null;
    }

    @Override
    public <T extends Serializable> T put(String key, T obj, int expireSeconds, String nameSpace) {
        return null;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public boolean delete(String key, String nameSpace) {
        return false;
    }

    @Override
    public boolean expireKey(String key, int seconds) {
        return false;
    }

    @Override
    public boolean expireKey(String key, int seconds, String nameSpace) {
        return false;
    }

    @Override
    public Long incr(String key, int count) {
        return null;
    }

    @Override
    public Long incr(String key, int count, String nameSpace) {
        return null;
    }

    @Override
    public Long decr(String key, int count) {
        return null;
    }

    @Override
    public Long decr(String key, int count, String nameSpace) {
        return null;
    }

    private String genKey(String key) {
        return genKey(key, namespace);
    }

    private String genKey(String key, String namespace) {
        return namespace + "_" + key;
    }

}
