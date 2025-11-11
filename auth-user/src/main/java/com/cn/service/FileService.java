package com.cn.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cn.entity.SysFile;
import com.cn.entity.User;
import com.cn.entity.UserProfile;
import com.cn.mapper.FileMapper;
import com.cn.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.relative-path}")
    private String relativePath;

    @Autowired
    UserService userService;

    @Autowired
    UserProfileService userProfileService;

    public String uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {
        // 解析上传用户
        String token = jwtUtils.extractToken(request);
        String username = jwtUtils.parseUsername(token);

        // 生成存储文件名
        String originalFilename = file.getOriginalFilename();
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storageName = UUID.randomUUID() + fileExt;

        // 存储路径处理
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) uploadPath.mkdirs();

        File dest = new File(uploadPath, storageName);
        file.transferTo(dest);

        // 保存文件记录
        SysFile sysFile = new SysFile();
        sysFile.setFileName(originalFilename);
        sysFile.setStorageName(storageName);
        sysFile.setFileExt(fileExt);
        sysFile.setFilePath(dest.getAbsolutePath());
        sysFile.setFileType(file.getContentType());
        sysFile.setFileSize(file.getSize());
        sysFile.setUploadUser(username);

        User user = userService.findByUsernameOrEmail(username);

        int i = fileMapper.insertFile(sysFile);
        if (i > 0) {
            // 只需要更新用户头像字段
            LambdaUpdateWrapper<UserProfile> lambda = new LambdaUpdateWrapper<>();
            lambda.set(UserProfile::getAvatar, relativePath + "/" + storageName) // 存储相对路径
                    .eq(UserProfile::getUserId, user.getId());
            userProfileService.update(lambda);
        }

        return relativePath + "/" + storageName; // 返回相对路径
    }
}