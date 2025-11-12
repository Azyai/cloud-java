package com.cn.controller;

import com.cn.entity.UserInfo;
import com.cn.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "个人中心系统", description = "个人中心管理模块")
@Validated  //开启参数验证
@RestController
@RequestMapping(value = "/api/user")
public class UserInfoController {


    @Resource
    UserInfoService userInfoService;
    @Operation(description = "完善个人信息")
    @PostMapping("/user-info")
    public String updateUserInfo(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        //插入个人信息到数据库中并返回
        return userInfoService.updateUserInfo(userInfo, request);
    }



}
