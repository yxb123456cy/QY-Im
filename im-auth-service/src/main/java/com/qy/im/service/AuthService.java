package com.qy.im.service;

import com.qy.im.dtos.UserLoginDTO;
import com.qy.im.response.Response;
import com.qy.im.vos.UserLoginRespVO;

public interface AuthService {

    //登录;
    Response<UserLoginRespVO> login(UserLoginDTO userLoginDTO);

    //注销
    Response<Boolean> logout();

    //认证;
    Response<UserLoginRespVO> authCheck();

    //QQ登录;

    //获取第三方登录信息;
}
