package com.cn;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Search8082 {
    public static void main(String[] args)
    {
        SpringApplication.run(Search8082.class,args);
    }
}