package com.rjgf.service;


import com.rjgf.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    //      用户注册
    Integer insertUser(User user);

//    判断用户名是否存在
    Boolean isUserNameTaken(String username);

}
