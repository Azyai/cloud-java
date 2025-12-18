package com.cn.redis.utils;

public class RedisConstants {
    public static final String BLOOM_FILTER_KEY = "bloom:filter";

    // 前缀示例，如果是在自己系统中，也可以添加这么一个静态值，方便管理
    // 例如，user系统，在yml中配置好后，直接写对应的业务前缀即可
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;

}
