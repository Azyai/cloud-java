package com.cn.service.impl;

import com.cn.entity.UserEvaluation;
import com.cn.mapper.UserEvaluationMapper;
import com.cn.service.UserEvaluationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEvaluationServiceImpl implements UserEvaluationService {
    @Autowired
    private UserEvaluationMapper userEvaluationMapper;
    @Override
    public String insertUserEvaluation(UserEvaluation userEvaluation, HttpServletRequest request) {
        userEvaluationMapper.insert(userEvaluation);
        return "用户评价插入成功";
    }

    @Override
    public String updateUserEvaluation(UserEvaluation userEvaluation, HttpServletRequest request) {
        userEvaluationMapper.updateById(userEvaluation);
        return "个人评价修改成功";
    }

    @Override
    public UserEvaluation showUserEvaluation(long id, HttpServletRequest request) {
        return userEvaluationMapper.selectById(id);
    }
}
