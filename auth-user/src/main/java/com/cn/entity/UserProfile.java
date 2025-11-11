package com.cn.entity;

import java.util.Date;

public class UserProfile {
    private Integer userId;          // 关联user表的id
    private String realName;         // 真实姓名
    private Integer gender;          // 性别:0-女,1-男,2-其他
    private Date birthDate;          // 出生日期
    private String phone;            // 手机号码
    private String address;          // 地址
    private String avatar;           // 头像URL
    private String bio;              // 个人简介
    private Date createdAt;          // 创建时间
    private Date updatedAt;          // 更新时间

    // 构造方法
    public UserProfile() {
    }

    // Getter和Setter方法
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString方法
    @Override
    public String toString() {
        return "UserProfileMapper{" +
                "userId=" + userId +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", avatar='" + avatar + '\'' +
                ", bio='" + bio + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}