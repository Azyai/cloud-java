package com.cn.service;

import com.cn.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {
    String insertUserInfo(UserInfo userInfo, HttpServletRequest request);

    String uploadAvatar(Long id, MultipartFile file, HttpServletRequest request);

    String updateUserInfo(UserInfo userInfo, HttpServletRequest request);

    UserInfo showUserInfo(long id, HttpServletRequest request);
}