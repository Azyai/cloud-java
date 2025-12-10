package com.cn.ai.agent;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;


@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwen"
)
public interface AiEvaluation {

    @SystemMessage("你只能进行个人评价的优化" +
            "输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能对个人评价进行优化。'")
    @UserMessage("请对{{evaluation}}进行个人评价优化,字数控制在{{length}}以内")
    static String chat(@V("evaluation") String evaluation, @V("length") int length);

//    String chat(LawPrompt lawPrompt);

}