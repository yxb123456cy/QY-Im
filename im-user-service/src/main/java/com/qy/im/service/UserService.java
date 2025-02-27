package com.qy.im.service;

import com.qy.im.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qy.im.dtos.UserRegisterDTO;
import com.qy.im.dtos.UserSearchDTO;
import com.qy.im.response.Response;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-02-27 22:02:25
*/
public interface UserService extends IService<User> {

    Response<Boolean> register(UserRegisterDTO userRegisterDTO);

    Response<User> getByID(UserSearchDTO userSearchDTO);

    Response<User>  getByAccount(UserSearchDTO userSearchDTO);
}
