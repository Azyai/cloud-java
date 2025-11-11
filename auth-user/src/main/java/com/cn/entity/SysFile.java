package com.cn.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SysFile {
    private Long id;
    private String fileName;
    private String storageName;
    private String fileExt;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private String uploadUser;
    private Date createTime;
}
