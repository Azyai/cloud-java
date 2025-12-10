package com.cn.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMModel {

    @Bean(name = "qwen")
    public ChatModel chatModelQwen(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }


    @Bean(name = "deepseek")
    public ChatModel chatModelDeepSeek(){
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("deepseek-api"))
                .modelName("deepseek-chat")
                .baseUrl("https://api.deepseek.com/v1")
                .build();
    }

}