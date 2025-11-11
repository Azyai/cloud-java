package com.cn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.entity.UserProfile;
import com.cn.resp.UserInfo;

public interface UserProfileService extends IService<UserProfile> {

    UserInfo findUserProfileByUserByUserNameOreMail(String userId);

}
