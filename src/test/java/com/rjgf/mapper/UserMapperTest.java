package com.rjgf.mapper;

import com.rjgf.entity.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void getUserByUserNameLogin() {
        User user = userMapper.login("evan", "123456");
        System.out.println(user);
    }

    @Test
    public void testGetUserByUserName() {
        User user = userMapper.getUserByUserName("evan");
        System.out.println(user);
    }
}