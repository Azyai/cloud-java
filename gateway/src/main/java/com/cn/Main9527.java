package com.cn;

import com.cn.config.MenuAuthoritiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //服务注册和发现
@EnableConfigurationProperties(MenuAuthoritiesConfig.class) // 配置类, 用于获取配置文件
public class Main9527 {
    public static void main(String[] args)
    {
        SpringApplication.run(Main9527.class,args);
    }
}