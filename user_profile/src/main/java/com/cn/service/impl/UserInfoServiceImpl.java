package com.cn.service.impl;

import com.cn.entity.UserInfo;
import com.cn.mapper.UserInfoMapper;
import com.cn.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Override
    public String updateUserInfo(UserInfo userInfo, HttpServletRequest request) {
        // 插入个人信息到数据库中并返回
        userInfoMapper.insert(userInfo);
        return "个人信息插入成功";
    }
}