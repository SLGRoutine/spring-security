package com.rjgf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: routine_cys
 * @CreateTime: 2025-02-13
 * @Description: 配置类
 */
@Configuration
@MapperScan("com.rjgf.mapper")
@ComponentScan
public class AppConfig {
}
