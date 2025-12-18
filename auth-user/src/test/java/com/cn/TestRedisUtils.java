package com.cn;


import com.cn.entity.User;
import com.cn.redis.utils.RedisUtils;
import com.cn.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestRedisUtils {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RedisUtils redisUtils;


    @Test
    public void testRedisUtils() {
        User admin = userService.findByUsernameOrEmail("cxk");

        System.out.println(admin);

        redisUtils.getStringValueWithPassThrough("user:username:cxk:",1,User.class, (c) -> userService.findByUsernameOrEmail("cxk"), 60L, TimeUnit.SECONDS);


    }
}
