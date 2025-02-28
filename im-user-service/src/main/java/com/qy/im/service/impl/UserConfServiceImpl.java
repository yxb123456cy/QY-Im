package com.qy.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qy.im.domain.UserConf;
import com.qy.im.service.UserConfService;
import com.qy.im.mapper.UserConfMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user_conf(用户配置表)】的数据库操作Service实现
* @createDate 2025-02-28 11:41:49
*/
@Service
public class UserConfServiceImpl extends ServiceImpl<UserConfMapper, UserConf>
    implements UserConfService{

}




