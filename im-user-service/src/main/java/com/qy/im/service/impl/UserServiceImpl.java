package com.qy.im.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qy.im.domain.User;
import com.qy.im.dtos.UserRegisterDTO;
import com.qy.im.dtos.UserSearchDTO;
import com.qy.im.response.Response;
import com.qy.im.service.UserService;
import com.qy.im.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-02-27 22:02:25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final UserMapper mapper;

    @Override
    public Response<Boolean> register(UserRegisterDTO userRegisterDTO) {
        //先检查是否存在该account;存在则注册失败,不存在则进行注册;
        String account = userRegisterDTO.getAccount();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount, account);
        User DBuser = mapper.selectOne(queryWrapper);
        if (ObjectUtil.isNotNull(DBuser)) {
            log.warn("account:{}已注册", account);
            return Response.fail("该用户已注册");
        }
        //insert;
        User user = new User();
        user.setAccount(account).setPassword(userRegisterDTO.getPassword());
        boolean saved = save(user);
        log.info("account 是否注册成功:{}", saved);
        return Response.success(saved);
    }

    //根据ID获取用户信息;
    @Override
    public Response<User> getByID(UserSearchDTO userSearchDTO) {
        User user = getById(userSearchDTO.getId());
        if (ObjectUtil.isNull(user)) {
            log.warn("ID为:{}的用户不存在", userSearchDTO.getId());
            return Response.fail("未查询到该用户");
        }
        return Response.success(user);
    }

    //根据account获取用户信息;
    @Override
    public Response<User> getByAccount(UserSearchDTO userSearchDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount, userSearchDTO.getAccount());
        User DBuser = mapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(DBuser)) {
            log.warn("account为:{}的用户不存在", userSearchDTO.getAccount());
            return Response.fail("未查询到该用户");
        }
        return Response.success(DBuser);
    }


}




