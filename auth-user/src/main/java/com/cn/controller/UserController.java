package com.cn.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cn.entity.User;
import com.cn.entity.UserProfile;
import com.cn.resp.ResultData;
import com.cn.resp.UserInfo;
import com.cn.service.FileService;
import com.cn.service.UserProfileService;
import com.cn.service.UserService;
import com.cn.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/auth/find")
public class UserController {

    @PreAuthorize("hasAuthority('2099')") // 只有拥有 '2099' 权限的用户才能访问
    @GetMapping("/test")
    public ResultData<String> test() {
        return ResultData.success("test");
    }


    @GetMapping("/test2")
    public ResultData<String> test2() {
        return ResultData.success("test2");
    }


    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserService userService;

    @Resource
    JwtUtils jwtUtils;

    @GetMapping("/GetUserId")
    public Long getUserId(@RequestParam("username") String username){
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUsername, username);
        User user = userService.getOne(lambdaUpdateWrapper);
        return Long.valueOf(user.getId());
    }


    @GetMapping("/getUserInfo")
    public ResultData<UserInfo> getUserInfo(HttpServletRequest request) {
        String token = extractToken(request);
        String username = jwtUtils.parseUsername(token);
        UserInfo userInfo = userProfileService.findUserProfileByUserByUserNameOreMail(username);
        return ResultData.success(userInfo);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Autowired
    FileService fileService;

    @PostMapping("/uploadAvatar")
    public ResultData<String> uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        System.out.println("文件名称" + file.getName());
        String relativePath = null; // 获取相对路径
        try {
            relativePath = fileService.uploadFile(file, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResultData.success(relativePath); // 返回相对路径
    }


    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping(value = "/getAvatar", produces = MediaType.IMAGE_JPEG_VALUE) // 根据实际图片类型调整
    public byte[] getAvatar(HttpServletRequest request) throws IOException {
        // 从请求中解析用户信息
        String token = jwtUtils.extractToken(request);
        String username = jwtUtils.parseUsername(token);

        // 获取用户信息
        UserInfo userInfo = userProfileService.findUserProfileByUserByUserNameOreMail(username);
        if (userInfo == null || userInfo.getAvatar() == null) {
            throw new RuntimeException("用户头像不存在");
        }

        // 获取头像的相对路径
        String avatarPath = userInfo.getAvatar();

        // 拼接完整的文件路径
        String fullPath = uploadDir + File.separator + avatarPath;
        File avatarFile = new File(fullPath);

        // 检查文件是否存在
        if (!avatarFile.exists()) {
            throw new RuntimeException("头像文件不存在");
        }

        // 读取文件并返回字节数组
        return Files.readAllBytes(avatarFile.toPath());
    }

    @PostMapping("updateUserInfo")
    public ResultData<String> updateUserInfo(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        LambdaUpdateWrapper<UserProfile> lambda = new LambdaUpdateWrapper<>();
        lambda.eq(UserProfile::getUserId, userInfo.getId())
                .set(UserProfile::getRealName, userInfo.getRealName())
                .set(UserProfile::getGender, userInfo.getGender())
                .set(UserProfile::getBirthDate, userInfo.getBirthDate())
                .set(UserProfile::getPhone, userInfo.getPhone())
                .set(UserProfile::getAddress, userInfo.getAddress())
                .set(UserProfile::getAvatar, userInfo.getAvatar())
                .set(UserProfile::getBio, userInfo.getBio());
        boolean update = userProfileService.update(lambda);
        if (!update){
            return ResultData.fail("更新失败,请联系管理员");
        }
        return ResultData.success("更新成功");
    }

}
