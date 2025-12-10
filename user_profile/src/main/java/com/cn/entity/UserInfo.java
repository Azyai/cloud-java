package com.cn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户信息表（简化版实体类）
 */
@Data
@TableName("user_info")
public class UserInfo {

    private Long id;  // 用户ID（自增主键）
    private Long userId;
    @NotBlank(message = "姓名不能为空")
    @Pattern(regexp = "^[\u4e00-\u9fa5a-zA-Z]{2,20}$", message = "姓名必须是2-20位中文或英文")
    private String name;      // 姓名
    
    @URL(message = "头像路径必须是有效的URL")
    private String avatar;    // 头像路径
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;     // 手机号
    
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;     // 邮箱
    
    @NotBlank(message = "微信号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]{6,20}$", message = "微信号必须是6-20位字母、数字或下划线")
    private String wechatId;  // 微信号

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^(男|女)$", message = "性别只能是男或女")
    private String gender;    // 性别（直接存字符串，如"男"/"女"）
    
    @NotNull(message = "出生日期不能为空")
    private LocalDate birthDate;  // 出生日期
    
    @NotBlank(message = "现居住地不能为空")
    @Length(max = 100, message = "现居住地长度不能超过100个字符")
    private String currentResidence;  // 现居住地

    private Boolean isEmployed = false;  // 是否就业（默认false）

    private Date timeEmployed; //求职时间
    @NotBlank(message = "最高学历不能为空")
    private String highestEducation;     // 最高学历
    
    @NotBlank(message = "求职状态不能为空")
    private String jobStatus;            // 求职状态



    @NotBlank(message = "民族不能为空")
    private String ethnicity;    // 民族
    
    @NotBlank(message = "政治面貌不能为空")
    private String politicalStatus;  // 政治面貌
    
    @NotBlank(message = "户口地不能为空")
    @Length(max = 100, message = "户口地长度不能超过100个字符")
    private String householdRegistration;  // 户口地

    @NotBlank(message = "婚姻状况不能为空")
    private String maritalStatus;  // 婚姻状况（如"未婚"/"已婚"）
    
    @NotBlank(message = "子女情况不能为空")
    private String hasChildren;    // 有无子女（如"有"/"无"）

    private LocalDateTime createdAt;  // 创建时间
    private LocalDateTime updatedAt;  // 更新时间
}