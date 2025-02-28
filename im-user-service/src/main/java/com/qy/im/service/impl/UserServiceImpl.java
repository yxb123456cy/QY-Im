package com.qy.im.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qy.im.domain.Friend;
import com.qy.im.domain.User;
import com.qy.im.domain.Verificationquestion;
import com.qy.im.dtos.*;
import com.qy.im.mapper.*;
import com.qy.im.response.Response;
import com.qy.im.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final FriendMapper friendMapper;
    private final FriendverifyMapper friendverifyMapper;
    private final UserConfMapper userConfMapper;
    private final VerificationquestionMapper verificationquestionMapper;

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
        log.info("account:{} 是否注册成功:{}", account, saved);
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
            this.logNotFoundUser(userSearchDTO.getAccount());
            return Response.fail("未查询到该用户");
        }
        return Response.success(DBuser);
    }

    private void logNotFoundUser(String account) {
        log.warn("account为:{}的用户不存在", account);
    }

    //更新用户基本信息;
    @Override
    public Response<Boolean> updateBaseInfo(UserUpdateInfoDTO userUpdateInfoDTO) {
        String account = userUpdateInfoDTO.getAccount();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getAccount, account);
        User DBuser = mapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(DBuser)) {
            this.logNotFoundUser(account);
            return Response.fail("未查询到该用户");
        }
        //存在则更新;
        DBuser.setNickname(userUpdateInfoDTO.getNickname()).setAvatar(userUpdateInfoDTO.getAvatar())
                .setDescription(userUpdateInfoDTO.getDescription());
        boolean updated = updateById(DBuser);
        return Response.success(updated);
    }

    @Override
    public Response<Boolean> saveVerifyQuestion(VerifyCationQuestionAddDTO dto) {
        Verificationquestion vq = new Verificationquestion();
        vq.setUserid(dto.getUserid()).setProblem(dto.getProblem()).setAnswer(dto.getAnswer());
        int inserted = verificationquestionMapper.insert(vq);
        return Response.success(inserted > 0);
    }

    //获取好友列表;
    @Override
    public Response<List<User>> getFriendList(Long masterId) {
        //构造条件;
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<Friend>()
                .eq(Friend::getMasterid, masterId);
        //获取friendList
        List<Friend> friendList = friendMapper.selectList(queryWrapper);
        //获取IDS集合;
        List<Long> ids = friendList.stream().map(Friend::getFriendid).toList();
        //获取userList
        List<User> userList = this.listByIds(ids);
        return Response.success(userList);
    }

    //修改好友备注;
    @Override
    public Response<Boolean> changeFriendNotice(ChangeNoticeDTO changeNoticeDTO) {
        CheckFriendDTO checkFriendDTO = new CheckFriendDTO();
        checkFriendDTO.setMasterId(changeNoticeDTO.getMasterId())
                .setFriendId(changeNoticeDTO.getFriendId());
        //判断是否为好友;
        Response<Boolean> checkResponse = this.checkFriend(checkFriendDTO);
        Boolean isFriend = checkResponse.getData();
        //非好友;
        if (!isFriend) {
            //不可修改 因为不是好友
            log.warn("修改好友备注失败,对方不是您的好友 friendID:{},masterID:{}", checkFriendDTO.getFriendId(), checkFriendDTO.getMasterId());
            return Response.fail("修改好友备注失败,对方不是您的好友");
        }
        //获取好友friend;
        //构建请求;
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<Friend>()
                .eq(Friend::getMasterid, changeNoticeDTO.getMasterId())
                .eq(Friend::getFriendid, changeNoticeDTO.getFriendId());
        Friend friend = friendMapper.selectOne(queryWrapper);

        //进行修改备注;
        friend.setNotice(changeNoticeDTO.getNewNotice());
        int updated = friendMapper.updateById(friend);
        //返回;
        return Response.success(updated > 0);
    }

    //用户搜索;
    @Override
    public Response<List<User>> searchUser(SearchUserDTO searchUserDTO) {
        return null;
    }

    //发起好友请求验证;
    @Override
    public Response<Boolean> sendFriendReq(AddFriendReqDTO addFriendReqDTO) {
        return null;
    }

    //进行好友请求验证;
    @Override
    public Response<?> ReqVerify(AddFriendReqDTO addFriendReqDTO) {
        return null;
    }

    //获取好友请求验证列表;
    @Override
    public Response<List<User>> getFriendReqList(Long masterID) {
        return null;
    }

    //删除好友;
    @Override
    public Response<Boolean> removeFriend(RemoveFriendDTO removeFriendDTO) {
        CheckFriendDTO checkFriendDTO = new CheckFriendDTO();
        checkFriendDTO.setMasterId(removeFriendDTO.getMasterId())
                .setFriendId(removeFriendDTO.getFriendId());
        //判断是否为好友;
        Response<Boolean> checkResponse = this.checkFriend(checkFriendDTO);
        Boolean isFriend = checkResponse.getData();
        //非好友;
        if (!isFriend) {
            //不可修改 因为不是好友
            log.error("删除好友失败,对方不是您的好友 friendID:{},masterID:{}", checkFriendDTO.getFriendId(), checkFriendDTO.getMasterId());
            return Response.fail("删除好友失败,对方不是您的好友");
        }
        //是好友 则进行删除;
        //构建请求;
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<Friend>()
                .eq(Friend::getMasterid, removeFriendDTO.getMasterId())
                .eq(Friend::getFriendid, removeFriendDTO.getFriendId());
        //进行删除;
        int deleted = friendMapper.delete(queryWrapper);
        return Response.success(deleted>0);
    }

    //判断是否为好友;
    @Override
    public Response<Boolean> checkFriend(CheckFriendDTO checkFriendDTO) {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<Friend>()
                .eq(Friend::getMasterid, checkFriendDTO.getMasterId())
                .eq(Friend::getFriendid, checkFriendDTO.getFriendId());
        Friend friend = friendMapper.selectOne(queryWrapper);
        return Response.success(ObjectUtil.isNotNull(friend));
    }

    //更新用户配置;
    @Override
    public Response<Boolean> updateUserConf(ChangeUserConfDTO changeUserConfDTO) {
        return null;
    }


}




