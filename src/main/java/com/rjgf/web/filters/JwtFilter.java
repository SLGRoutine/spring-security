package com.rjgf.web.filters;


import com.rjgf.service.impl.UserServiceImpl;
import com.rjgf.utils.JwtTotenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

//此过滤器的作用：1.是否有token    2.token是否过期   3.验证用户名和密码是否匹配
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTotenUtil jwtTotenUtil;    // 注入jwt工具类
    private final UserServiceImpl userService;  // 注入用户服务类

    @Autowired
    @Lazy
    public JwtFilter(JwtTotenUtil jwtTotenUtil, UserServiceImpl userService) {
        this.jwtTotenUtil = jwtTotenUtil;
        this.userService = userService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            String uri = request.getRequestURI();

            // 如果请求是 /security/loginByPwd，则直接放行，不进行 JWT 认证
//            if (uri.equals("/security/loginByPwd")) {
//                filterChain.doFilter(request, response);
//                return;
//            }

        String jwtToken = request.getHeader("token");

        if (jwtToken != null && jwtToken.length() != 0 && !jwtTotenUtil.isTokenExpired(jwtToken)) {   //TODO isTokenExpired
            try{ //token可用

                Claims claims = jwtTotenUtil.decodeJWTWithKey(jwtToken);            //todo
                String tel = claims.get("username").toString();                //todo
                UserDetails user = ((UserDetailsService)userService).loadUserByUsername(tel);
                if (user!= null){
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            user,null,user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }catch (Exception e){
                log.info(e.getMessage());
            }
        }else {
            log.warn("token is null or empty or out of time,probably user is not login in !");
        }
        filterChain.doFilter(request,response);     //继续过滤
    }



}
