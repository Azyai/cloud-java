package com.cn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_role")
public class UserRole {
    int id;
    int userId;
    int roleId;
}
