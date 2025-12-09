package com.cn.ai.entity;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.Data;

@Data
//@StructuredPrompt("在{{legal}}长度限制下，进行优化个人评价：{{evaluation}}")
public class LawPrompt
{
    private String evaluation;
    private int length;
}