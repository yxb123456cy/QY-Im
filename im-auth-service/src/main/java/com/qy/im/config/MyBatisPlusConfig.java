package com.qy.im.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.qy.im.mapper")
public class MyBatisPlusConfig {
}
