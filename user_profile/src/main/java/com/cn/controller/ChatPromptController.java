package com.cn.controller;


import com.cn.ai.agent.AiEvaluation;

import com.cn.ai.entity.LawPrompt;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Tag(name = "ai评价优化", description = "ai评价优化模块")
@RestController
@RequestMapping(value = "/api/user")
public class ChatPromptController {
    @Resource
    private AiEvaluation aiEvaluation;

    @PostMapping(value = "/AiEvaluation")
    public String AiEvaluation(@RequestBody LawPrompt lawPrompt) {
        System.out.println("请求数据：" + lawPrompt);
        
        // 检查参数是否为空
        if (lawPrompt == null) {
            System.out.println("LawPrompt对象为null");
            return "请求数据不能为空";
        }
        
        System.out.println("evaluation值：" + lawPrompt.getEvaluation());
        System.out.println("length值：" + lawPrompt.getLength());
        
        if (lawPrompt.getEvaluation() == null || lawPrompt.getEvaluation().trim().isEmpty()) {
            return "评价内容不能为空";
        }
        
        return aiEvaluation.chat(lawPrompt.getEvaluation(), lawPrompt.getLength());
    }
    
    @GetMapping(value = "/test")
    public String test1() {
        String chat = aiEvaluation.chat("拥有多年的工作经验，曾参与多个大型项目，积累了丰富的项目管理经验。擅长协调团队资源，确保项目按时完成并达到预期目标。",500);
        System.out.println(chat);

        return "success : "+ new Date() +" \n\n chat: "+chat+" \n\n ";
    }
}