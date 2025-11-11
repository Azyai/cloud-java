package com.cn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
//      根据用户角色列表查询菜单权限，会涉及到中间表
    List<String> findAuthoritiesByRoleName(List<String> roleNames);
}
