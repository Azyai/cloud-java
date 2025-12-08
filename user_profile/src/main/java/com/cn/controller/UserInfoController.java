package com.cn.controller;

import com.cn.entity.UserInfo;
import com.cn.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "个人中心系统", description = "个人中心管理模块")
@Validated  //开启参数验证
@RestController
@RequestMapping(value = "/api/user")
public class UserInfoController {


    @Resource
    UserInfoService userInfoService;
    @Operation(description = "插入个人信息")
    @PostMapping("/insertUserInfo")
    public String insertUserInfo(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        return userInfoService.insertUserInfo(userInfo, request);
    }
    @Operation(description = "修改个人信息")
    @PostMapping("/updateUserInfo")
    public String updateUserInfo(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        return userInfoService.updateUserInfo(userInfo, request);
    }
    @Operation(description = "展示个人信息")
    @GetMapping("/showUserInfo")
    public UserInfo showUserInfo(@RequestParam("id") long id, HttpServletRequest request) {
        return userInfoService.showUserInfo(id, request);
    }
    
    @Operation(description = "上传个人头像")
    @PostMapping("/upload")
    public String uploadAvatar(@RequestParam("id") Long id,
                              @RequestParam("avatar") MultipartFile file, 
                              HttpServletRequest request) {
        //根据用户ID上传头像文件并保存访问地址到数据库中
        return userInfoService.uploadAvatar(id, file, request);
    }

}