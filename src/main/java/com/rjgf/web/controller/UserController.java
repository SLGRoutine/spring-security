package com.rjgf.web.controller;


import com.rjgf.entity.User;
import com.rjgf.entity.web.model.ResponseResult;
import com.rjgf.service.UserService;
import com.rjgf.utils.JwtTotenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/security")
@Slf4j
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;        // 认证管理器

    @Autowired
    private JwtTotenUtil jwtTotenUtil;        // 生成token对象

    @Autowired
    private UserService userService;        // 业务层对象


    /**
     * 登录
     */
    @PostMapping("/loginByPwd")
    public ResponseResult login(@RequestBody User user) {
        String token = "";
        try {
            Authentication authentication = authenticationManager.authenticate(
                    //这里会调用UserDetailsServiceImpl的loadUserByUsername方法
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            //将认证信息存入安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //获取认证信息
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            //获取登录用户信息
            User loginUser = (User) userDetails;

            //生成JWT负载
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("username", userDetails.getUsername());
            payloadMap.put("id", loginUser.getId().toString());         //用户id
            payloadMap.put("role", "user");                          //用户角色
            //生成token
            token = jwtTotenUtil.encodeJWT(payloadMap);
        } catch (Exception e) {
            log.error("登录失败", e);
            return ResponseResult.failure(e.getMessage());
        }
        return ResponseResult.success("登录成功").setData(token);
    }


    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        try {
            if (userService.isUserNameTaken(user.getUsername())) {
                return ResponseResult.failure("用户名已存在");
            }
//            service层
            userService.insertUser(user);
        } catch (Exception e) {
            log.error("注册失败", e);
            return ResponseResult.failure(e.getMessage());
        }
        return ResponseResult.success("注册成功");
    }


    /**
     * 登录状态检查
     */
    @GetMapping("/getLoginStatus")
    public ResponseResult getLoginStatus(){

        log.info("登录状态检查");
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("用户信息："+user);
        user.setPassword("");
        return ResponseResult.success("验证成功").setData(user);
    }

    @PostMapping("/getUser")
    public ResponseResult getUser(@RequestBody User user){
        return ResponseResult.success("获取成功");
    }

    
}
