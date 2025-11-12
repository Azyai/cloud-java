package com.cn.entity;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUser {

    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,18}$", message = "用户名格式不正确")
    private String username;

    @Size(min = 6, max = 18, message = "密码长度必须在6-18位之间")
    private String password;

    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", message = "邮箱格式不正确")
    private String email;

    @Size(min = 6, max = 6, message = "验证码必须为6位")
    private String code;
}