package com.example.springboot_quartz.redis;

import java.io.Serializable;

/**
 * Created by pengfeihu on 16/3/7.
 */
public interface Cache {

    /**
     * Get the object by key, object can be a String,
     * String list, String set, String hash.
     * @param key the cache key
     * @param <T> returned object
     * @return
     */
    public <T extends Serializable> T get(String key) ;

    public <T extends Serializable> T get(String key,String nameSpace) ;

    /**
     * put the entire Object to redis, if redis exists the same key,
     * will delete it, be careful if use this method, this is a replace method.
     * 这个方法将用传入的key和obj, 替换掉缓存上对应的key(针对list, set, hashset),
     *
     * @param key the key in cache
     * @param obj return obj if success, return null if failed.
     * @param expireSeconds seconds  -1:永不过期
     * @param <T>
     * @return
     */
    public <T extends Serializable> T put(String key, T obj,int expireSeconds) ;

    public <T extends Serializable> T put(String key, T obj,int expireSeconds,String nameSpace) ;

    /**
     * 删除掉cache上对应key的内容, 小心使用, 如果只想删掉对应列表中对应key
     * 的某一项, 请使用remove方法.
     * @param key key in cache
     * @return success return true
     */
    public boolean delete(String key);

    public boolean delete(String key,String nameSpace);

    /**
     * 设置某个key的过期时间
     *
     * @param key
     * @param seconds
     * @return true:设置成功 false:失败
     */
    public boolean expireKey(String key, int seconds);

    public boolean expireKey(String key, int seconds,String nameSpace);



    /**
     * 原子加法
     *
     * @param key
     * @param count
     * @return
     */
    public Long incr(String key, int count);

    public Long incr(String key, int count,String nameSpace);

    /**
     * 原子减法
     *
     * @param key
     * @param count
     * @return
     */
    public Long decr(String key, int count);

    public Long decr(String key, int count,String nameSpace);

}
