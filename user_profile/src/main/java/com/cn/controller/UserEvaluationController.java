package com.cn.controller;

import com.cn.entity.UserEvaluation;
import com.cn.service.UserEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "个人评价", description = "个人评价模块")
@Validated  //开启参数验证
@RestController
@RequestMapping(value = "/api/user")
public class UserEvaluationController {
@Resource
UserEvaluationService userEvaluationService;
    @Operation(description = "插入个人评价")
    @PostMapping("/insertUserEvaluation")
    public String insertUserEvaluation(@RequestBody UserEvaluation userEvaluation, HttpServletRequest request) {
        return userEvaluationService.insertUserEvaluation(userEvaluation, request);
    }
    @Operation(description = "修改个人评价")
    @PostMapping("/updateUserEvaluation")
    public String updateUserEvaluation(@RequestBody UserEvaluation userEvaluation, HttpServletRequest request) {
        return userEvaluationService.updateUserEvaluation(userEvaluation, request);
    }
    @Operation(description = "展示个人评价")
    @PostMapping("/showUserEvaluation")
    public UserEvaluation showUserEvaluation(@RequestParam("id") long id, HttpServletRequest request) {
        return userEvaluationService.showUserEvaluation(id, request);
    }
}
