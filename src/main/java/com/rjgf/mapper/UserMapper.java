package com.rjgf.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjgf.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User getUserByUserName(@Param("username") String username);


    /**
     * 注册用户
     * @param user
     * @return
     */
    Integer insertUser( User user);

}
