package com.qy.im.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qy.im.domain.User;
import com.qy.im.dtos.UserLoginDTO;
import com.qy.im.mapper.UserMapper;
import com.qy.im.response.Response;
import com.qy.im.service.AuthService;
import com.qy.im.vos.UserLoginRespVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper mapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String LOGOUT_TOKEN_CACHE = "LOGOUT_TOKEN_CACHE";
    private final static String PASSWORD_SALT = "ELASTICSEARCH_FJX";

    @Override
    public Response<UserLoginRespVO> login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        String password = userLoginDTO.getPassword();
        //校验账号与密码;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount, account);
        User user = mapper.selectOne(queryWrapper);
        if (ObjectUtil.isNotNull(user)) {
            //不为空 则比对密码;
            if (user.getPassword().equals(userLoginDTO.getPassword())) {
                //比对成功;
                //SaToken登录
                StpUtil.login(user.getId());
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                UserLoginRespVO respVO = new UserLoginRespVO();
                respVO.setAccount(account).setUserId(user.getId());
                respVO.setTokenValue(tokenInfo.getTokenValue());//设置token值
                respVO.setTokenName(tokenInfo.getTokenName());//设置token-Name;
                log.info("account为:{}的用户登录成功", account);
                return Response.success(respVO);
            }
            log.warn("account为:{}的用户登录失败,密码错误,请检查密码", account);
            return Response.fail("密码错误");

        }
        log.error("未查找到account为:{}的用户信息", account);
        return Response.fail("未查找到该用户");
    }

    @Override
    public Response<Boolean> logout() {
        boolean isLogin = StpUtil.isLogin();//获取是否登录;
        if (!isLogin) {
            //未登录 不可注销;
            log.warn("未登录 不可注销");
            return Response.fail("您还未登录");
        }
        //已登录情况;
        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
        StpUtil.logout();
        //将该token加入到注销缓存中;
        CompletableFuture.runAsync(() -> {
            redisTemplate.boundSetOps(LOGOUT_TOKEN_CACHE).add(tokenValue);
        }).join();
        return Response.success(true);
    }

    @Override
    public Response<UserLoginRespVO> authCheck() {
        String tokenValue = StpUtil.getTokenValue();
        boolean login = StpUtil.isLogin();
        Boolean aBoolean = redisTemplate.boundSetOps(LOGOUT_TOKEN_CACHE).isMember(tokenValue);
        if (aBoolean != null) {
            log.info("是否存在于注销缓存中:{}", aBoolean);
        }
        if (aBoolean != null && aBoolean.equals(true)) {
            //当前token无效;
            log.error("token无效");
            return Response.fail("token无效");
        }
        log.info("当前TOKEN不存在于注销缓存中");
        if (!login) {
            log.error("当前账号未登录");
            return Response.fail("当前账号未登录");
        }
        //已登录;
        long userID = StpUtil.getLoginIdAsLong(); //获取用户ID;
        //通过ID查询;
        User user = mapper.selectById(userID);
        UserLoginRespVO respVO = new UserLoginRespVO();
        respVO.setUserId(user.getId()).setTokenValue(tokenValue)
                .setAccount(user.getAccount()).setTokenName("token");
        log.info("用户:{}登录认证成功", user.getAccount());
        return Response.success(respVO);
    }
}
