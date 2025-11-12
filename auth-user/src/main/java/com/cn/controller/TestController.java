package com.cn.controller;


import com.cn.entity.Role;
import com.cn.entity.dto.PageInfo;
import com.cn.entity.dto.PageWrapper;
import com.cn.resp.ApiResponse;
import com.cn.resp.ResultData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RequestMapping模块上的地址是以api开头
 * 第二个是 auth代表是auth-user这个微服务
 * 第三个则是某一块功能的简单描述
 *
 * 数据流转整体尽量还是要按照po、vo、dto、request进行分层
 */
@RestController
@RequestMapping("/auth-user/api/test")
public class TestController {

    /**
     * 所有数据的返回均是ResultData对象
     * 带分页的数据则是ResultData<>
     */

    /**
     * 普通成功数据返回
     * @return
     */
    @GetMapping("/test1")
    public ResultData<String> test1() {
        return ApiResponse.success("test1");
    }

    /**
     * 成功，但无需返回响应信息，则返回默认code200和成功信息
     * @return
     */
    @GetMapping("/test2")
    public ResultData test2() {
        return ApiResponse.success();
    }

    /**
     * 成功，返回所有的数据信息
     * @return
     */
    @GetMapping("/test3")
    public ResultData<List<Role>> test3() {
        List<Role> roles = null;
        for (int i = 0; i < 10; i++){
            Role role = new Role();
            role.setId((long) i);
            role.setName("role" + i);
            role.setDescription("role" + i + "描述");
            roles.add(role);
        }

        // 这里仅是做模拟，真实的场景要去数据库查询
        return ApiResponse.success(roles);
    }


    /**
     * 成功，返回分页的数据信息
     * @return
     */
    @GetMapping("/test4")
    public ResultData<PageWrapper<Role>> test4() {
        // 这里仅是做模拟，真实的场景要去数据库查询
        List<Role> roles = null;
        for (int i = 0; i < 10; i++){
            Role role = new Role();
            role.setId((long) i);
            role.setName("role" + i);
            role.setDescription("role" + i + "描述");
            roles.add(role);
        }
        PageWrapper<Role> pageWrapper = new PageWrapper<>();
        pageWrapper.setTbody(roles);
        pageWrapper.setPageInfo(new PageInfo(1L, 10L, 10L));
        pageWrapper.setIsMulti(false);

        return ApiResponse.success(pageWrapper);
    }

    /**
     * 失败，返回错误信息，仅提供code（根据code去查询对应的错误信息）
     * 具体查看ReturnCodeEnum.getReturnCodeEnumV2()方法
     * @return
     */
    @GetMapping("/test5")
    public ResultData<PageWrapper<Role>> test5() {
        return ApiResponse.fail("400");
    }

    /**
     * 失败，返回错误信息，提供错误码和错误信息
     * 具体查看ReturnCodeEnum.getReturnCodeEnumV2()方法
     * @return
     */
    @GetMapping("/test6")
    public ResultData<PageWrapper<Role>> test6() {
        return ApiResponse.fail("400","认证错误");
    }

}