package com.cn.securityservice.impl;

import com.cn.securityservice.RbacService;
import com.cn.service.RoleMenuService;
import com.cn.service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RbacServiceImpl implements RbacService {

    @Resource
    private UserRoleService userRoleService;

    @Override
    public List<String> findRolesByUsername(String userName) {
        return userRoleService.findRolesByUsername(userName);
    }

    //根据角色列表查询菜单权限，肯定会涉及到中间表
    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<String> findAuthoritiesByRoleName(List<String> roleNames) {
        return roleMenuService.findAuthoritiesByRoleName(roleNames);
    }

}
