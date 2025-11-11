package com.cn.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role_menu")  // 指定数据库表名
public class RoleMenu {
    @TableId  // 标记为主键字段
    private Integer id;
 
    @TableField("menu_id")  // 显式映射数据库列名
    private Integer menuId;
 
    @TableField("role_id")  // 显式映射数据库列名
    private Integer roleId;
}