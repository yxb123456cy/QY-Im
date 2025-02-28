package com.qy.im.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
@MapperScan(basePackages = "com.qy.im.mapper")
@Configuration
public class MybatisPlusConfig {
}
