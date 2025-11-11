package com.cn.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtilsGateWay {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 直接传入 token 字符串进行验证
    public boolean validateToken(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
                return false;
            }

            return !isInBlacklist(token);
        } catch (JwtException e) {
            return false;
        }
    }

    // 解析用户名
    public String parseUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 加入黑名单
    public void addToBlacklist(String token) {
        String jti = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();

        if (jti == null) {
            jti = UUID.randomUUID().toString();
        }

        long expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime() - System.currentTimeMillis();

        redisTemplate.opsForValue().set("blacklist:token:" + jti, "true", expiration, TimeUnit.MILLISECONDS);
    }

    // 检查是否在黑名单
    public boolean isInBlacklist(String token) {
        String jti = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getId();

        if (jti == null) return false;

        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:token:" + jti));
    }
}
