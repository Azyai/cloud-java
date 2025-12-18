package com.cn.redis.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ay
 * &#064;description:  Redis工具类 使用示例可以参考search-anything
 * &#064;date  2025-12-16 17:41
 */
@Component("redisUtils")
@Qualifier
@Slf4j
public class RedisUtils {

    // redis key的前缀，需要在你当前服务的application.yml配置
    // 它会自动读取并设置好前缀的数据信息
    @Value("${spring.redis.prefix:}")
    private String prefixConfig;

    private String resolvedPrefix;

    @Value("${spring.redis.expire:432000}")
    private Long expireTime;

    @PostConstruct
    public void init() {
        this.resolvedPrefix = Optional.ofNullable(prefixConfig).orElse("");
    }

    public String formatKey(String key) {
        return resolvedPrefix + key;
    }

    private List<String> formatKey(String... keys) {
        return Arrays.stream(keys)
                .map(this::formatKey)
                .collect(Collectors.toList());
    }


    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisBloomFilterUtils redisBloomFilterUtils;

    @Autowired
    @Qualifier("hashOperations")
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    @Qualifier("valueOperations")
    private ValueOperations<String, Object> valueOperations;

    @Autowired
    @Qualifier("listOperations")
    private ListOperations<String, Object> listOperations;

    @Autowired
    @Qualifier("setOperations")
    private SetOperations<String, Object> setOperations;

    @Autowired
    @Qualifier("zSetOperations")
    private ZSetOperations<String, Object> zSetOperations;

    @Autowired
    @Qualifier("geoOperations")
    private GeoOperations<String, Object> geoOperations;

    @Autowired
    @Qualifier("hyperLogLogOperations")
    private HyperLogLogOperations<String, Object> hyperLogLogOperations;


    // ========commons公共操作========

    /**
     * 判断key是否存在
     */
    public Boolean hasKey(String key){
        try{
            return redisTemplate.hasKey(formatKey(key));
        }catch (Exception e){
            log.error("RedisUtils.hasKey error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 判断多个key是否存在
     */
    public Boolean hasKeys(String... keys){
        try{
            List<String> formattedKeys = formatKey(keys);
            Long count = redisTemplate.countExistingKeys(formattedKeys);
            return count == formattedKeys.size();
        }catch (Exception e){
            log.error("RedisUtils.hasKeys error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 删除key
     */
    public Boolean deleteKey(String key){
        try{
            return redisTemplate.delete(formatKey(key));
        }catch (Exception e){
            log.error("RedisUtils.deleteKey error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 批量删除key
     */
    public Long deleteKeys(String... keys){
        try{
            List<String> formattedKeys = formatKey(keys);
            return redisTemplate.delete(formattedKeys);
        }catch (Exception e){
            log.error("RedisUtils.deleteKeys error: {}", e.getMessage());
        }
        return 0L;
    }

    /**
     * 删除匹配模式的key
     */
    public Long deletePattern(String pattern){
        Set<String> keys = redisTemplate.keys(formatKey(pattern));
        try{
            if(!keys.isEmpty()){
                return redisTemplate.delete(keys);
            }
        }catch (Exception e){
            log.error("RedisUtils.deletePattern error: {}", e.getMessage());
        }
        return 0L;
    }

    /**
     * 设置对应key的过期时间，单位时间秒
     */
    public Boolean expire(String key,long seconds){
        try{
            return redisTemplate.expire(formatKey(key), seconds, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("RedisUtils.expire error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 设置对应key的过期时间，单位时间分钟
     */
    public Boolean expireMinutes(String key,long minutes){
        try{
            return redisTemplate.expire(formatKey(key), minutes, TimeUnit.MINUTES);
        }catch (Exception e){
            log.error("RedisUtils.expireMinutes error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 设置对应key的过期时间，单位时间毫秒
     */
    public Boolean expireMillis(String key,long millis){
        try{
            return redisTemplate.expire(formatKey(key), millis, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("RedisUtils.expireMillis error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取对应key的剩余过期时间，单位毫秒
     */
    public Long getExpireTime(String key){
        try{
            return redisTemplate.getExpire(formatKey(key), TimeUnit.MILLISECONDS);
        }catch (Exception e){
            log.error("RedisUtils.getExpireTime error: {}", e.getMessage());
        }
        return 0L;
    }

    /**
     * 移除过期时间，使key永久有效
     */
    public Boolean persist(String key){
        try{
            return redisTemplate.persist(formatKey(key));
        }catch (Exception e){
            log.error("RedisUtils.persist error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 获取key的类型
     */
    public DataType type(String key){
        try{
            return redisTemplate.type(formatKey(key));
        }catch (Exception e){
            log.error("RedisUtils.type error: {}", e.getMessage());
        }
        return null;
    }

    // ========string操作========
    /**
     * 设置缓存，带有过期时间
     */
    public <T> void setStringValue(String key,T value,Long time,TimeUnit unit){
        try{
            valueOperations.set(formatKey(key),value,time,unit);
        }catch (Exception e){
            log.error("RedisUtils.setStringValue error: {}", e.getMessage());
        }
    }

    /**
     * 设置逻辑过期时间
     */
    public <T> void setStringValueWithLogicExpire(String key,T value,Long time,TimeUnit unit){
        try{
            RedisData<T> redisData = new RedisData<>();
            redisData.setData(value);
            redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
            valueOperations.set(formatKey(key),redisData);
        }catch (Exception e){
            log.error("RedisUtils.setStringValueWithLogicExpire error: {}", e.getMessage());
        }
    }

    /**
     * 防止缓存穿透，一般是使用这个方法
     */
    public <T,ID> T getStringValueWithPassThrough(String key,ID id, Class<T> type,
            Function<ID,T>  dbFallback,Long time,TimeUnit unit){
        try{
            String formatKey = formatKey(key);
            formatKey = formatKey + id;

            // 1.从redis中尝试获取数据
            Object redisData =  valueOperations.get(formatKey);
            if(redisData != null && !"".equals(redisData)){
                return (T)redisData;
            }

            // 2.命中缓存，但是是空对象
            if ("".equals(redisData)){
                return null;
            }

            // 3.不存在则从数据库中查询
            T dbData = dbFallback.apply(id);
            if(dbData == null){
                // 3.1数据库中不存在，则将空值写入Redis
                valueOperations.set(formatKey,"",time,unit);
                return null;
            }

            // 4.若数据库存在，则将数据写入redis中
            valueOperations.set(formatKey,dbData,time,unit);
            return dbData;

        }catch (Exception e){
            log.error("RedisUtils.getStringValueWithLogicExpire error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 尝试获取锁
     */
    public Boolean tryLock(String lockKey,String requestId,long expireSeconds){
        return valueOperations.setIfAbsent(formatKey(lockKey),requestId,expireSeconds,TimeUnit.SECONDS);
    }

    /**
     * 释放锁
     */
    public Boolean unLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(formatKey(lockKey)),
                requestId
        );
        return result != null && result == 1L;
    }

    /**
     * 线程池
     */
    private static final ExecutorService CACHE_REBUILD_EXECUTOR
            = Executors.newFixedThreadPool(10);

    /**
     * 逻辑过期 + 异步刷新 防止缓存击穿
     */
    // 逻辑过期具体实现
    public <T,ID>  T queryShopByIdWithLogicExpire(String keyPrefix,String lockKeyPrefix, ID id, Class<T> type,
                                                  Function<ID,T> dbFallback, Long time, TimeUnit unit) {
        String key = keyPrefix + id;

        // 1.从Redis中查询缓存
        String shopJson = stringRedisTemplate.opsForValue().get(formatKey(key));

        if (StrUtil.isBlank(shopJson)) {
            return null;
        }

        // 2.命中缓存，先把json反序列化为对象
        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
        T t = JSONUtil.toBean((JSONObject) redisData.getData(), type);

        // 3.判断是否过期  4.未过期，直接返回
        if (redisData.getExpireTime().isAfter(LocalDateTime.now())) {
            return t;
        }

        // 5.已经过期，需要缓存重建
        // 5.1获取互斥锁
        String lockKey = lockKeyPrefix + id;

        // 5.2判断是否获取锁成功
        boolean isLock = tryLock(lockKey, id.toString(), 10);

        // 5.3获取锁成功，开启独立线程，异步缓存重建
        if (isLock) {
            // 开启独立线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try {
                    // 先查数据库
                    T t1 = dbFallback.apply(id);

                    // 再写入redis重建缓存
                    this.valueOperations.set(key, t1,time,unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unLock(lockKey, id.toString());
                }
            });
        }

        // 5.4返回过期的数据
        return t;
    }


}