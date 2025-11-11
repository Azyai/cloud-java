package com.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.entity.UserProfile;
import com.cn.mapper.UserProfileMapper;
import com.cn.resp.UserInfo;
import com.cn.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper,UserProfile> implements UserProfileService {

    @Autowired
    UserProfileMapper userProfileMapper;

    @Override
    public UserInfo findUserProfileByUserByUserNameOreMail(String userId) {
        UserInfo userInfo = userProfileMapper.findUserProfileByUserByUserId(userId);

        return userInfo;
    }
}
