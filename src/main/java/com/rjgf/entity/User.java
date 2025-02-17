package com.rjgf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("tb_user")
public class User implements UserDetails, Serializable  {
    @TableId
    private Integer id;
    @TableField("user_name") // 数据库字段
    private String username;
    private String password;
    private String name;
    private Integer age;
    private Integer sex;
    private Date birthday;
    private String created;
    private String updated;

    @Override  //用户所有的权限 ， ，返回的列表中至少得有一个值，否则这个用户啥权限都没有
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("user"));
    }

    /**
     *  需要注意的是，如果密码和用户名字段 不为username ，password ，则需要重写
     *  getPassword()
     *  getUsername()        ========》这两个方法为UserDetails接口必须的方法
     *  并返回指定的字段值
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}