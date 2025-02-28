package com.qy.im;

import com.anwen.mongo.annotation.MongoMapperScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * IM 对话微服务;
 */
@EnableRabbit
@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@MongoMapperScan(value = "com.qy.im.MongoMapper")
@EnableFeignClients
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
        log.info("ChatApplication Running");

    }

}
