package com.cn.resp;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 用于返回用户信息
 */
@Data
@ToString
public class UserInfo {
    private Integer id;  // 用户id
    private String username; // 账号
    private String email; // 邮箱
    private String realName;         // 真实姓名
    private Integer gender;          // 性别:0-女,1-男,2-其他

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;          // 出生日期
    private String phone;            // 手机号码
    private String address;          // 地址
    private String avatar;           // 头像URL
    private String bio;              // 个人简介
}
