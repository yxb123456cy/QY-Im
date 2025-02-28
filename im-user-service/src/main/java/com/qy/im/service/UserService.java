package com.qy.im.service;

import com.qy.im.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qy.im.dtos.*;
import com.qy.im.response.Response;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2025-02-27 22:02:25
 */
public interface UserService extends IService<User> {
    //用户注册
    Response<Boolean> register(UserRegisterDTO userRegisterDTO);

    //通过ID获取用户信息
    Response<User> getByID(UserSearchDTO userSearchDTO);

    //通过账号获取信息
    Response<User> getByAccount(UserSearchDTO userSearchDTO);

    //修改用户信息;
    Response<Boolean> updateBaseInfo(UserUpdateInfoDTO userUpdateInfoDTO);

    //保存用户验证问题;
    Response<Boolean> saveVerifyQuestion(VerifyCationQuestionAddDTO verifyCationQuestionAddDTO);

    //获取好友列表;
    Response<List<User>> getFriendList(Long masterId);

    //修改好友备注;
    Response<Boolean> changeFriendNotice(ChangeNoticeDTO changeNoticeDTO);

    //用户搜索;
    Response<List<User>> searchUser(SearchUserDTO searchUserDTO);

    //用户发起添加好友请求验证;
    Response<Boolean> sendFriendReq(AddFriendReqDTO addFriendReqDTO);

    //进行验证;
    Response<?> ReqVerify(AddFriendReqDTO addFriendReqDTO);

    //获取申请添加好友列表;
    Response<List<User>> getFriendReqList(Long masterID);

    //删除好友;
    Response<Boolean> removeFriend(RemoveFriendDTO removeFriendDTO);

    //判断是否是好友;
    Response<Boolean> checkFriend(CheckFriendDTO checkFriendDTO);

    //更新用户配置;
    Response<Boolean> updateUserConf(ChangeUserConfDTO changeUserConfDTO);

}
