package com.msb.common.cache;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

/**
 * ylw
 * @author ylw
 * @date 18-4-19 下午3:13
 * @param
 * @return
 */
public interface RedisService {

    /**
     * 获取缓存<br>
     * 注：基本数据类型(Character除外)，请直接使用get(String key, Class<T> clazz)取值
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取set集合
     * @param key
     * @return
     */
    Set<String> getSetMembers(String key);

    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 将value对象写入缓存
     * @param key
     * @param value
     * @param expireTime 失效时间(秒)
     */
    void set(String key, String value, long expireTime);

    /**
     * 递减操作
     * @param key
     * @param by
     * @return
     */
    double decr(String key, double by);

    /**
     * 递增操作
     * @param key
     * @param by
     * @return
     */
    double incr(String key, double by);

    /**
     * 递减操作
     * @param key
     * @param by
     * @return
     */
    double decr(String key, long by);

    /**
     * 递增操作
     * @param key
     * @param by
     * @return
     */
    double incr(String key, long by);

    /**
     * 递减操作
     * @param key
     * @param by
     * @return
     */
    double decr(String key, int by);

    /**
     * 递增操作
     * @param key
     * @param by
     * @return
     */
    double incr(String key, int by);

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     * @param key
     */
    void del(String... key);

    /**
     * 指定缓存的失效时间
     * @author FangJun
     * @date 2016年8月14日
     * @param key 缓存KEY
     * @param expireTime 失效时间(秒)
     */
    void expire(String key, long expireTime);

    /**
     * 添加set
     * @param key
     * @param value
     */
    void sadd(String key, String... value);

    /**
     * 删除set集合中的对象
     * @param key
     * @param value
     */
    void srem(String key, String... value);

    /**
     * set重命名
     * @param oldkey
     * @param newkey
     */
    void srename(String oldkey, String newkey);

    /**
     * 模糊查询keys
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 范围获取zset集合
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<ZSetOperations.TypedTuple<String>> zrangeWithScores(String key, long start, long end);

    Set<ZSetOperations.TypedTuple<String>> zreverseRangeWithScores(String key, long start, long end);

    /**
     * zset: 返回指定key对应的有序集合中，索引在min~max之间的元素信息，如果带上 withscores 属性的话，可以将分值也带出来
     * @param key
     * @param start
     * @param end
     * @return
     */
    Set<String> zrange(String key, long start, long end);

    /**
     * 添加zset
     * @param key
     * @param value
     */
    void zsadd(String key, String value, double score);

    /**
     * zset:返回指定key中的集合中指定member元素对应的分值
     * @param key
     * @param member
     */
    double zscore(String key, Object member);

    /**
     * zset:返回指定key对应的集合中，指定member在其中的排名，注意排名从0开始且按照分值从大到小降序
     * @param key
     * @param member
     */
    long zrevrank(String key, Object member);

    /**
     * zset:返回指定key对应的有序集合的元素数量
     */
    long zcard(String key);
}
