package com.qy.im.controller;

import com.qy.im.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 /* 1. 登录接口
    2. 认证接口
    3. 注销接口
    4. 第三方登录信息接口，用于在前端展示第三方登录
    5. 第三方登录接口*/
/**
 * Auth认证相关控制器;
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


}
