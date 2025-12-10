package com.cn.service.impl;

import com.cn.entity.UserInfo;
import com.cn.mapper.UserInfoMapper;
import com.cn.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    //插入个人信息
    @Override
    public String insertUserInfo(UserInfo userInfo, HttpServletRequest request) {

        // 插入个人信息到数据库中并返回
        userInfoMapper.insert(userInfo);
        return "个人信息插入成功";
    }
    //上传头像
    @Override
    public String uploadAvatar(Long id, MultipartFile file, HttpServletRequest request) {
        //根据用户ID上传头像到本地D:\\uploads文件夹内并返回访问地址保存到数据库中
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return "上传失败，文件为空";
            }
            
            // 创建上传目录（如果不存在）
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // 获取原始文件名并生成唯一文件名
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;
            
            // 构建目标文件路径
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
            
            // 将文件复制到目标位置
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // 构建可访问的URL（根据实际部署环境可能需要调整）
            String fileUrl = "/uploads/" + fileName;
            
            // 根据ID更新用户头像路径
            UserInfo userInfo = userInfoMapper.selectById(id);
            if (userInfo != null) {
                userInfo.setAvatar(fileUrl);
                userInfo.setUpdatedAt(LocalDateTime.now());
                userInfoMapper.updateById(userInfo);
                
                System.out.println("用户ID " + id + " 的头像上传成功，访问地址: " + fileUrl);
                return "用户ID " + id + " 的头像上传成功，访问地址: " + fileUrl;
            } else {
                // 如果用户不存在，则删除刚刚上传的文件
                File uploadedFile = new File(targetLocation.toString());
                if (uploadedFile.exists()) {
                    uploadedFile.delete();
                }
                return "用户ID " + id + " 不存在，上传失败";
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return "头像上传失败: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "头像上传失败: " + e.getMessage();
        }
    }
    //修改个人信息
    @Override
    public String updateUserInfo(UserInfo userInfo, HttpServletRequest request) {
        // 修改个人信息到数据库中并返回
        userInfo.setUpdatedAt(LocalDateTime.now());
        userInfoMapper.updateById(userInfo);
        return "个人信息修改成功";
    }
    //展示个人信息
    @Override
    public UserInfo showUserInfo(long id, HttpServletRequest request) {
        //展示个人信息
        return userInfoMapper.selectById(id);
    }
}