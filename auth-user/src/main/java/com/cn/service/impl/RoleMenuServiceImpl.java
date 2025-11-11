package com.cn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.entity.RoleMenu;
import com.cn.mapper.RoleMenuMapper;
import com.cn.service.RoleMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {


    @Override
    public List<String> findAuthoritiesByRoleName(List<String> roleNames) {
        return this.baseMapper.findAuthoritiesByRoleName(roleNames);
    }
}
