package com.cn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class URLConfig implements WebMvcConfigurer {
    /**
     * * 资源映射路径     *  * addResourceHandler：访问映射路径     *  * addResourceLocations：资源绝对路径     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("Configuring resource handlers...");
        registry.addResourceHandler("/static/uploads/**")
                .addResourceLocations("file:D:/Project/Java2025/uploads/");
    }
}