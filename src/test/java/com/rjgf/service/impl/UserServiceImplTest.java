package com.rjgf.service.impl;

import com.rjgf.AppConfig;
import com.rjgf.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Before
    public void setUp() throws Exception {
    }


    @Test
    public void setbCryptPasswordEncoder() {
    }

    @Test
    public void loadUserByUsername() {
        User user = (User) userService.loadUserByUsername("evan");
        System.out.println(user);
    }

    @Test
    public void insertUser() {
    }

    @Test
    public void isUserNameTaken() {
        System.out.println();
    }
}