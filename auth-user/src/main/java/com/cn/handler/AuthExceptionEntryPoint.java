package com.cn.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.cn.resp.ResultData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    /**
     * 当用户未登录时调用此方法向客户端返回未授权的错误信息
     *
     * @param request       客户端请求对象，用于获取请求信息
     * @param response      服务器响应对象，用于向客户端发送响应数据
     * @param authException 认证异常，包含未授权的具体信息
     *                      此方法主要负责在用户尝试访问需要登录才能访问的资源时，
     *                      向客户端返回一个未授权的错误响应，提示用户需要先进行登录
     *                      它通过设置响应的状态码为401（未授权），并返回一个包含错误信息的JSON对象，
     *                      以告知客户端具体的错误原因
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        System.out.println("认证异常: "+ authException.getMessage());

        // 设置响应内容类型为JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 设置响应字符编码为UTF-8
        response.setCharacterEncoding("UTF-8");
        // 设置响应状态码为401未授权
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 创建一个失败的响应结果对象，包含错误代码和消息
        ResultData<String> result = ResultData.fail("401", "请先登录");
        // 创建一个ObjectMapper对象，用于转换Java对象为JSON格式
        ObjectMapper mapper = new ObjectMapper();

        // 将ResultData对象转换为JSON字符串并写入响应体中
        response.getWriter().write(mapper.writeValueAsString(result));
    }

}
