package com.cn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * 用户评价表
 * 用于存储用户对服务的评价信息，包括评价内容、标签等
 */
@Data
@TableName("user_evaluation")
public class UserEvaluation {
    /**
     * 评价ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    /**
     * 评价内容，最多500字
     */
    @Size(max = 500, message = "评价内容不能超过500个字符")
    private String content;
    
    /**
     * 评价标签，用于分类
     */
    @Size(max = 100, message = "标签不能超过100个字符")
    private String tags;
    
    /**
     * 创建时间
     */
    private Date createdAt;
    
    /**
     * 更新时间
     */
    private Date updatedAt;
}