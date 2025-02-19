package com.rjgf.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rjgf.entity.User;
import com.rjgf.mapper.UserMapper;
import com.rjgf.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = false)
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired              //利用set注入   防止 循环依赖
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private PasswordEncoder bCryptPasswordEncoder;  // 密码加密  注入 （当前项目并未使用）

    @Autowired
    public void setbCryptPasswordEncoder(PasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 根据用户名查询用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User user = null;
            user = userMapper.getUserByUserName(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户 " + username + " 不存在");
            }

            return user;
        } catch (Exception e) {
            log.error("用户 " + username + " 不存在", e);
            throw new UsernameNotFoundException("用户 " + username + " 不存在", e);
        }
    }

    //    用户注册
    public Integer insertUser(User user) {
        int result = userMapper.insertUser(user);
        return result;
    }

    //    判断用户名是否存在
    @Override
    public Boolean isUserNameTaken(String username) {
        return userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)) > 0;
    }


}
