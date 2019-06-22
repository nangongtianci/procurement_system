package com.msb.common.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 常用工具类
 * @author ylw
 * @date 18-4-17 下午1:16
 * @param
 * @return
 */
@Component
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 取得缓存
     * @param key
     * @return
     */
    public String get(String key){
        String value = stringRedisTemplate.boundValueOps(key).get();
        if(StringUtils.isNotBlank(value)){
            return String.valueOf(value);
        }
        return null;
    }



    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void set(String key, String value, long expireTime) {
        stringRedisTemplate.opsForValue().set(key,value);
        if(expireTime>0){
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 递减操作
     * @param key
     * @param by
     * @return
     */
    public double decr(String key, double by){
        return redisTemplate.opsForValue().increment(key, -by);
    }
    public double decr(String key, long by){
        return redisTemplate.opsForValue().increment(key, -by);
    }
    public double decr(String key, int by){
        return redisTemplate.opsForValue().increment(key, -by);
    }

    /**
     * 递增操作
     * @param key
     * @param by
     * @return
     */
    public double incr(String key, double by){
        return redisTemplate.opsForValue().increment(key, by);
    }
    public double incr(String key, long by){
        return redisTemplate.opsForValue().increment(key, by);
    }
    public double incr(String key, int by){
        return redisTemplate.opsForValue().increment(key, by);
    }

    /**
     * 指定缓存的失效时间
     * @param key 缓存KEY
     * @param expireTime 失效时间(秒)
     */
    public void expire(String key, long expireTime) {
        if(expireTime > 0){
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 添加set
     * @param key
     * @param value
     */
    public void sadd(String key, String... value) {
        redisTemplate.boundSetOps(key).add(value);
    }

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     * @param key
     */
    public void del(String... key){
        if(key!=null && key.length > 0){
            if(key.length == 1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除set集合中的对象
     * @param key
     * @param value
     */
    public void srem(String key, String... value) {
        redisTemplate.boundSetOps(key).remove(value);
    }

    /**
     * set重命名
     * @param oldkey
     * @param newkey
     */
    public void srename(String oldkey, String newkey){
        redisTemplate.boundSetOps(oldkey).rename(newkey);
    }

    /**
     * 获取set集合
     * @param key
     * @return
     */
    @Override
    public Set<String> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 模糊查询keys
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }


    /**------------------zset集合操作--------------------**/
    /**
     * 范围获取zset集合
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Set<ZSetOperations.TypedTuple<String>> zrangeWithScores(String key,long start,long end) {
        return redisTemplate.boundZSetOps(key).rangeWithScores(start,end);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<String>> zreverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).reverseRangeWithScores(start,end);
    }

    /**
     * zset: 返回指定key对应的有序集合中，索引在min~max之间的元素信息，如果带上 withscores 属性的话，可以将分值也带出来
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Set<String> zrange(String key, long start, long end) {
        return redisTemplate.boundZSetOps(key).range(start,end);
    }

    /**
     * zset: 添加元素
     * @param key
     * @param value
     */
    @Override
    public void zsadd(String key,String value,double score) {
        redisTemplate.boundZSetOps(key).add(value,score);
    }

    /**
     * zset:返回指定key中的集合中指定member元素对应的分值
     * @param key
     * @param member
     */
    @Override
    public double zscore(String key, Object member) {
        return redisTemplate.boundZSetOps(key).score(member);
    }

    /**
     * zset:返回指定key对应的集合中，指定member在其中的排名，注意排名从0开始且按照分值从大到小降序
     * @param key
     * @param member
     */
    @Override
    public long zrevrank(String key, Object member) {
        return redisTemplate.boundZSetOps(key).reverseRank(member)==null?-2:redisTemplate.boundZSetOps(key).reverseRank(member);
    }

    /**
     * zset:返回指定key对应的有序集合的元素数量
     */
    @Override
    public long zcard(String key) {
        return redisTemplate.boundZSetOps(key).zCard();
    }
}
