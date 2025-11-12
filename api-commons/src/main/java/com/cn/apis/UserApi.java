package com.cn.apis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "cloud-user",path = "/user")
public interface UserApi {


    @GetMapping("/GetUserId")
    Long getUserId(@RequestParam("username") String username);

}
