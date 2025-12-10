package com.cn.service;

import com.cn.entity.UserEvaluation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


public interface UserEvaluationService {
    String insertUserEvaluation(UserEvaluation userEvaluation, HttpServletRequest request);

    String updateUserEvaluation(UserEvaluation userEvaluation, HttpServletRequest request);

    UserEvaluation showUserEvaluation(long id, HttpServletRequest request);
}
