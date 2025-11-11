package com.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.entity.User;
import com.cn.mapper.UserMapper;
import com.cn.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findByUsernameOrEmail(String username) {
        return this.baseMapper.findByUsernameOrEmail(username);
    }
}
