package com.cn.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cn.config.MenuAuthoritiesConfig;
import com.cn.resp.ResultData;
import com.cn.utils.JwtUtilsGateWay;
import com.cn.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 自定义网关过滤器工厂类，用于在 Spring Cloud Gateway 中实现 JWT 鉴权逻辑。
 * 该类继承 AbstractGatewayFilterFactory 并注册为 Spring Bean。
 */
@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {

    @Autowired
    private JwtUtilsGateWay jwtUtilsGateWay;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MenuAuthoritiesConfig menuAuthoritiesConfig;


    public AuthGatewayFilterFactory() {
        super(Config.class); // 确保 Config 类被正确初始化
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // 1. 白名单路径放行
            if (isWhiteList(request.getPath().value())) {
                return chain.filter(exchange);
            }

            // 2. 提取 Token
            String authHeader = request.getHeaders().getFirst("Authorization");
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (token == null) {
                return unauthorized(exchange, "Missing token");
            }

            // 3. 验证 Token
            if (!jwtUtilsGateWay.validateToken(token)) {
                return unauthorized(exchange, "Invalid or expired token");
            }

            // 4. 获取用户名
            String username = jwtUtilsGateWay.parseUsername(token);
            if (username == null) {
                return unauthorized(exchange, "Invalid token");
            }

            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User",username)
                    .build();

            // 5. 查询 Redis 用户权限码 List<String>
            String authorityStr = redisTemplate.opsForValue().get("user:" + username + ":authorities");
            if (authorityStr == null || authorityStr.isEmpty()) {
                return forbidden(exchange, "No authorities found for user");
            }

            List<String> authorities = Arrays.asList(authorityStr.split(","));
            if (authorities == null || authorities.isEmpty()) {
                return forbidden(exchange, "No authorities found for user");
            }

            System.out.println("authorities: " + authorities);

            // 6. 判断是否有访问权限
            String path = request.getPath().value();
            if (!hasAuthorities(path, authorities)) {
                return forbidden(exchange, "Access denied to this resource");
            }

            // 7. 放行
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private boolean isWhiteList(String path) {
        return Arrays.stream(SecurityConstants.WHITE_LIST).anyMatch(path::startsWith);
    }

    private boolean hasAuthorities(String path, Collection<String> authorities) {
        String permissionCode = resolveAuthoritiesCode(path);

        System.out.println("permissionCode: " + permissionCode);
        if (permissionCode == null) {
            return false;
        }

        // 检查是否为超级管理员
        if (authorities.contains("2099")) {
            return true;
        }

        // 检查当前权限码或其父权限码是否存在
        return authorities.contains(permissionCode) || hasParentAuthority(permissionCode, authorities);
    }

    private boolean hasParentAuthority(String permissionCode, Collection<String> authorities) {
        Optional<MenuAuthoritiesConfig.MenuAuthority> currentMenu = menuAuthoritiesConfig.getAuthorities().stream()
                .filter(menu -> menu.getAclValue().equals(permissionCode))
                .findFirst();

        if (currentMenu.isPresent()) {
            int parentId = currentMenu.get().getPId();
            if (parentId != -1) {
                Optional<MenuAuthoritiesConfig.MenuAuthority> parentMenu = menuAuthoritiesConfig.getAuthorities().stream()
                        .filter(menu -> menu.getId() == parentId)
                        .findFirst();
                if (parentMenu.isPresent()) {
                    String parentAclValue = parentMenu.get().getAclValue();
                    return authorities.contains(parentAclValue) || hasParentAuthority(parentAclValue, authorities);
                }
            }
        }
        return false;
    }

    private String resolveAuthoritiesCode(String path) {
        return menuAuthoritiesConfig.getAuthorities().stream()
                .filter(menu -> path.startsWith(menu.getUrl()))
                .map(MenuAuthoritiesConfig.MenuAuthority::getAclValue)
                .findFirst()
                .orElse(null);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ResultData<?> resultData = ResultData.fail("401", message);
        return writeResponse(exchange, HttpStatus.UNAUTHORIZED, resultData);
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        ResultData<?> resultData = ResultData.fail("403", message);
        return writeResponse(exchange, HttpStatus.FORBIDDEN, resultData);
    }

    private Mono<Void> writeResponse(ServerWebExchange exchange, HttpStatus status, ResultData<?> resultData) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(resultData);
        } catch (JsonProcessingException e) {
            bytes = "{\"code\":\"500\",\"message\":\"Internal server error\"}".getBytes();
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {
        // 可以用于配置参数，例如指定某些路径需要特定角色等

    }
}