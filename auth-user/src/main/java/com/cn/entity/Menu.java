package com.cn.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu")  // 指定数据库表名
public class Menu {
    @TableId  // 标记为主键字段
    private Integer id;
 
    @TableField("icon")  // 显式指定数据库列名（可选，驼峰命名会自动映射）
    private String icon;
 
    @TableField("name")
    private String name;
 
    @TableField("state")
    private Integer state;
 
    @TableField("url")
    private String url;
 
    @TableField("p_id")  // 数据库列名是 p_id，与实体类字段名不同，需显式映射
    private Integer pId;
 
    @TableField("acl_value")
    private String aclValue;
 
    @TableField("grade")
    private Integer grade;
 
    @TableField("is_del")
    private Integer isDel;
}