package com.qy.im.controller;

import com.qy.im.domain.User;
import com.qy.im.dtos.*;
import com.qy.im.response.Response;
import com.qy.im.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/updateBaseInfo")
    //修改用户信息;
    public Response<Boolean> updateBaseInfo(@RequestBody UserUpdateInfoDTO userUpdateInfoDTO) {
        return userService.updateBaseInfo(userUpdateInfoDTO);
    }

    //保存用户验证问题;
    @PostMapping("/saveVerifyQuestion")
    public Response<Boolean> saveVerifyQuestion(@RequestBody VerifyCationQuestionAddDTO verifyCationQuestionAddDTO) {
        return userService.saveVerifyQuestion(verifyCationQuestionAddDTO);
    }
    //获取好友列表;
    @GetMapping("/getFriendList")
    public Response<List<User>> getFriendList(@RequestParam("masterId") Long masterId) {
        return userService.getFriendList(masterId);
    }

    //修改好友备注;
    @PostMapping("/changeFriendNotice")
    public Response<Boolean> changeFriendNotice(@RequestBody ChangeNoticeDTO changeNoticeDTO) {
        return userService.changeFriendNotice(changeNoticeDTO);
    }

    //用户搜索;
    @PostMapping("/searchUser")
    public Response<List<User>> searchUser(@RequestBody SearchUserDTO searchUserDTO) {
        return null;
    }

    //用户发起添加好友请求验证;
    @PostMapping("/sendFriendReq")
    public Response<Boolean> sendFriendReq(@RequestBody AddFriendReqDTO addFriendReqDTO) {
        return null;
    }

    //进行验证;
    @PostMapping("/ReqVerify")
    public Response<?> ReqVerify(@RequestBody AddFriendReqDTO addFriendReqDTO) {
        return null;
    }

    //获取申请添加好友列表;
    @GetMapping("/getFriendReqList")
    public Response<List<User>> getFriendReqList(@RequestParam("masterId") Long masterID) {
        return null;
    }

    //删除好友;
    @DeleteMapping("/removeFriend")
    public Response<Boolean> removeFriend(@RequestBody RemoveFriendDTO removeFriendDTO) {
        return userService.removeFriend(removeFriendDTO);
    }

    //判断是否是好友;
    @GetMapping("/checkFriend")
    public Response<Boolean> checkFriend(@RequestBody CheckFriendDTO checkFriendDTO) {
        return userService.checkFriend(checkFriendDTO);
    }

    //更新用户配置;
    @PostMapping("/updateUserConf")
    public Response<Boolean> updateUserConf(@RequestBody ChangeUserConfDTO changeUserConfDTO) {
        return null;
    }


}
