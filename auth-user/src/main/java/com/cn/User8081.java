package com.cn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class User8081 {
    public static void main(String[] args)
    {
        SpringApplication.run(User8081.class,args);
    }
}