package com.cn.securityservice;

import java.util.List;


public interface RbacService {
    // RbacService这个接口用于角色的鉴权
    /**
     * @Description: 根据用户名查询分配的角色
     * @Param: [username]
     */
    List<String> findRolesByUsername(String userName);


    List<String> findAuthoritiesByRoleName(List<String> roleNames);

}
