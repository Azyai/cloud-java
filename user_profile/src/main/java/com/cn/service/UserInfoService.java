package com.cn.service;

import com.cn.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

public interface UserInfoService {
    String updateUserInfo(UserInfo userInfo, HttpServletRequest request);
}
