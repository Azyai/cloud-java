package com.cn.config;


import com.cn.filter.JwtAuthenticationFilter;
import com.cn.handler.AuthExceptionEntryPoint;
import com.cn.securityservice.AuthorizeService;
import com.cn.utils.SecurityConstants;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//     在 Spring Security 5.7 及以上版本中，AuthenticationManagerBuilder 的 and() 方法已被标记为弃用，并计划在未来版本中移除
//     这是因为 Spring Security 团队正在推动更清晰的 API 设计，避免链式调用可能导致的歧义
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(authorizeService);
        return authBuilder.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Resource
    DataSource dataSource;

    @Bean
    PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 第一次true自动创建一个表，我们也可以手动创建(首次需要，其他就不需要了，再次运行就要改为false)
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    private AuthExceptionEntryPoint authExceptionEntryPoint;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(SecurityConstants.WHITE_LIST);
    }


    // 只是编写了配置文件，还没有写登录成功的重定向302
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, PersistentTokenRepository tokenRepository) throws Exception {
        http
//                .cors(cors -> {
//                    cors.configurationSource(corsConfigurationSource());
//                })
            .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(SecurityConstants.WHITE_LIST).permitAll()
                                .anyRequest().authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authExceptionEntryPoint))
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(remember -> remember.rememberMeParameter("remember")
                        .tokenRepository(tokenRepository) //这里要将其注入才能自动创建表
                        .tokenValiditySeconds(3600 * 24 * 7 ) //以秒计算，7天内免登录
                );

        return http.build();
    }


//    private CorsConfigurationSource corsConfigurationSource(){
//
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        // 允许我们携带cookie，因为前端我们是设置了的发送cookie
//        corsConfiguration.setAllowCredentials(true);
//        // 设置允许的请求头
//        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
//        // 设置允许请求的方法
//        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
//
//        // corsConfiguration.addAllowedOriginPattern("*");
//        // 设置能允许请求的路径 这两个都可以设置单个或全部，只不过下面这个可以设置多个允许地址
//
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://127.0.0.1:5173"));
//        corsConfiguration.setMaxAge(3600L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        // 所有的请求都走我们这个策略
//        source.registerCorsConfiguration("/**",corsConfiguration);
//        return source;
//    }


//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
//                        .allowedMethods("*")
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
//            }
//        };
//    }


}