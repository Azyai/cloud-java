package com.cn.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@RefreshScope  // 确保添加此注解
@ConfigurationProperties(prefix = "menu-authorities")
public class MenuAuthoritiesConfig {

    private List<MenuAuthority> authorities;

    @Data
    public static class MenuAuthority {
        private Integer id;
        private String icon;
        private String name;
        private Integer state;
        private String url;
        private Integer pId;
        private String aclValue;
        private Integer grade;
    }

    @PostConstruct
    public void init() {
        System.out.println("Loading authorities from Nacos...");
        if (authorities == null) {
            System.out.println("Authorities is null, check Nacos configuration.");
        } else {
            System.out.println("Loaded authorities: " + authorities);
        }
    }
}
