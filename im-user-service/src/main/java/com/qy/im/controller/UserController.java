package com.qy.im.controller;

import com.qy.im.domain.User;
import com.qy.im.dtos.UserRegisterDTO;
import com.qy.im.dtos.UserSearchDTO;
import com.qy.im.response.Response;
import com.qy.im.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * User相关控制器;
 */
@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //用户注册;
    @PostMapping("/register")
    public Response<Boolean> register(@RequestBody UserRegisterDTO dto) {
        return userService.register(dto);
    }

    //根据ID获取用户信息;
    @GetMapping("/getById")
    public Response<User> getByID(@RequestBody UserSearchDTO dto) {
        return userService.getByID(dto);
    }

    //根据account获取用户信息;
    @GetMapping("/getByAccount")
    public Response<User> getByAccount(@RequestBody UserSearchDTO dto) {
        return userService.getByAccount(dto);
    }
    //当前登录人的个人信息的获取和更新接口
    //我的好友列表接口
    //修改好友备注接口
    //用户搜索接口
    //当前登录人申请加好友
    //好友验证列表、好友验证操作


}
