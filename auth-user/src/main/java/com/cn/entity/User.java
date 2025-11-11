package com.cn.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
//@EqualsAndHashCode(callSuper = false)
public class User implements Serializable, UserDetails {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Boolean enabled;


    @TableField(value = "accountNonExpired")
    private boolean accountNonExpired;
    @TableField(value = "accountNonLocked")
    private boolean accountNonLocked;

    @TableField(value = "credentialsNonExpired")
    private boolean credentialsNonExpired;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    Collection<? extends GrantedAuthority> authorities;

    @TableField(exist = false)
    private UserProfile userProfile;


    @TableField(exist = false)
    private String roleIds;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        for (Role role : getRoles()) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//        return authorities;
//    }

    public Integer getId() {
        return id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
