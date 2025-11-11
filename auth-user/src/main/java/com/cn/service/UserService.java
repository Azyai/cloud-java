package com.cn.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.entity.User;

public interface UserService extends IService<User> {
    User findByUsernameOrEmail(String username);
}
