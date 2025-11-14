package com.cn.controller;


import com.cn.ai.agent.AiEvaluation;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "ai评价优化", description = "ai评价优化模块")
@Validated  //开启参数验证
@RestController
@RequestMapping(value = "/api/user")
public class ChatPromptController {
    @Resource
    private AiEvaluation aiEvaluation;

    @PostMapping(value = "/AiEvaluation")
    public String AiEvaluation(@RequestBody String evaluation,@RequestBody int length) {
        return AiEvaluation.chat(evaluation,length);
    }
}