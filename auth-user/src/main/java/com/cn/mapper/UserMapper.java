package com.cn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findByUsernameOrEmail(String text);

    @Insert("insert into user(username, password, email,enabled,accountNonExpired,accountNonLocked,credentialsNonExpired) values(#{username},#{password},#{email},1,1,1,1)")
    int createAccount(@Param("username") String username, @Param("password")String password, @Param("email")String email);


    @Update("update user set password = #{password} where email = #{email}")
    int resetPasswordByEmail(@Param("password") String password,@Param("email") String email);

}
