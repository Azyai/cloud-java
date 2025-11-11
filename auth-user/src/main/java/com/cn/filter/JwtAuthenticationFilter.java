package com.cn.filter;

import com.cn.utils.JwtUtils;
import com.cn.utils.SecurityConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;

    @Autowired
    StringRedisTemplate redisTemplate;



    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private boolean isWhiteListed(String path, HttpServletRequest request) {
        return Arrays.stream(SecurityConstants.WHITE_LIST)
                .anyMatch(pattern -> antPathMatcher.match(pattern, path))
                || "OPTIONS".equals(request.getMethod()); // 添加 OPTIONS 方法检查
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isWhiteListed(path,request)) {
            filterChain.doFilter(request, response);
            System.out.println(path + "已经放行");
            return;
        }

        // 只有在非白名单路径时才进行 Token 校验
        String token = extractToken(request);

        if (token == null || !jwtUtils.validateToken(token) || jwtUtils.isInBlacklist(token)) {
            filterChain.doFilter(request, response); // 让 Spring Security 统一处理认证失败
            return;
        }

//        System.out.println("token校验通过");

        String username = jwtUtils.parseUsername(token);
        String authorityStr = redisTemplate.opsForValue().get("user:" + username + ":authorities");

        if (authorityStr == null || authorityStr.isEmpty()) {
            filterChain.doFilter(request, response); // 让 Spring Security 统一处理
            return;
        }

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorityStr);
        Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);

//        System.out.println("从Redis获取的权限字符串：" + authorityStr); // 应包含'2099'
//        System.out.println(authorities);
//        System.out.println("用户权限已缓存到 Redis");
//        System.out.println(auth);

        filterChain.doFilter(request, response);
    }


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
