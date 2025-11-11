package com.cn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.entity.UserProfile;
import com.cn.resp.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    UserInfo findUserProfileByUserByUserId(String userId);

}
