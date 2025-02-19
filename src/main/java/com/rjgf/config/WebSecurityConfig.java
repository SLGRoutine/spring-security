package com.rjgf.config;

import com.rjgf.service.impl.UserServiceImpl;
import com.rjgf.web.filters.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {


    @Autowired
    private JwtFilter jwtFilter;

    private  UserServiceImpl userService;
    @Autowired
    private void setUserService(@Lazy UserServiceImpl userService) {
        this.userService = userService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {  // 密码加密
//        return  new BCryptPasswordEncoder();  // 加密
        return NoOpPasswordEncoder.getInstance();       // 不加密
    }


    @Bean   // 认证提供者
    public AuthenticationProvider    authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //这里是自己实现的UserDetailsService(需要重写loadUserByUsername方法)
        daoAuthenticationProvider.setUserDetailsService(userService);
        //设置密码加密方式
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return  daoAuthenticationProvider;
    }

    @Bean   // 认证管理器
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 配置安全拦截机制   ，  配置拦截的请求路径，  配置哪些请求需要认证，哪些请求不需要认证
     *  1.配置哪些请求需要具有什么样的角色权限才能访问
     *  2.配置哪些请求需要认证，哪些请求不需要认证          ======>目前支持
     *  3.配置哪些请求需要具有什么样的角色权限才能访问
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        //处理不同请求的权限
                        request.requestMatchers(HttpMethod.POST, "/security/loginByPwd","/security/register").permitAll() // 允许访问登录接口
                                .requestMatchers(HttpMethod.GET,"/security/none").permitAll()
                                .anyRequest().authenticated()   // 其他请求("anyRequest()")需要认证("authenticated()")
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//        将用户授权时用到的JWT校验过滤器加进SecurityFilterChain中，并放在UsernamePasswordAuthenticationFilter之前
        return httpSecurity.build();
    }

}
