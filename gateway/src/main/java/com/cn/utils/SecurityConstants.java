package com.cn.utils;

public class SecurityConstants {
    public static final String[] WHITE_LIST = {
            "/api/auth/user/login",
            "/api/auth/user/valid-register-email",
            "/api/auth/user/valid-reset-email",
            "/api/auth/user/start-reset",
            "/api/auth/user/do-reset",
            "/api/auth/user/valid-email",
            "/api/auth/user/fx",
            "/api/auth/user/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/**",
            "/v3/**",
            "/webjars/**",
            "/error",
            "/static/**",
            // 放行图标
            "/favicon.ico",
            "/draw/activity/selectActivityInfo"
    };
}