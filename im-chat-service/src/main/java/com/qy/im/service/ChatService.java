package com.qy.im.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.qy.im.MongoMapper.MonGoMessageDao;
import com.qy.im.config.RabbitMQConfig;
import com.qy.im.document.MonGoMessage;
import com.qy.im.domain.Message;
import com.qy.im.dtos.MessageDTO;
import com.qy.im.dtos.UserLoginRespVO;
import com.qy.im.mapper.MessageMapper;
import com.qy.im.response.Response;
import com.qy.im.rpc.AuthRpc;
import com.qy.im.rpc.UserRpc;
import com.qy.im.ws.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final MessageMapper messageMapper;
    private final UserRpc userRpc;
    private final AuthRpc authRpc;
    private final MonGoMessageDao monGoMessageDao;
    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final static String MESSAGE_HISTORY_CACHE_PREFIX = "MESSAGE_HISTORY:";
    private final static String MESSAGE_HISTORY_UUID_SALT = "eyj-fjx";
    //用户给好友发送消息;

    /**
     * 1.用户(Master需在线) 校验当前用户是否登录
     * 2. Master建立ws连接 进入聊天界面就已经建立好了Master连接;
     * 3.对方得是你的好友(未删除) 但对方可以不在线; //校验对方是否为好友
     * 4.发送消息 快速响应;
     * 5.Rabbitmq异步接耦 生产消息到相对应队列(若对方在线则不加入死信队列 否则加入死信队列 等待用户上线后进行正确拉取消费);保证事务消息;
     * 6.保存至MongoDB;
     * 7. 拉取消息进行消费 并进行通知(根据是否在线有不同的逻辑; 根据是否建立ws也有不同的逻辑;)
     */
    //调用日志服务;
    //鉴黄接口;

    //校验当前用户是否登录
    private boolean checkMasterHaveLogin(Long userID) {
        Response<UserLoginRespVO> response = authRpc.checkUserHaveLogin(userID);
        return response != null && response.isSuccess() && response.getData().getTokenValue() != null;
    }

    //校验是否在线;
    private boolean getCurrentUserOnlineStatus(Long masterID) {
        WebSocketServer server = WebSocketServer.getWebSocketHashMap().get(masterID);
        return ObjectUtil.isNotNull(server);
    }

    //判断对方是否存在
    private boolean userExists(Long userId) {
        Response<Boolean> response = userRpc.userExists(userId);
        return response != null && response.isSuccess() && response.getData().equals(true);
    }

    //校验是否为好友
    private boolean checkFriend(Long masterID, Long userID) {
        Response<Boolean> response = userRpc.checkFriend(masterID, userID);
        return response != null && response.isSuccess() && response.getData().equals(true);
    }

    //获取好友的在线状态 在线就建立websocket连接 不在线则不建立;
    private boolean getFriendOnlineStatus(Long friendID) {
        WebSocketServer server = WebSocketServer.getWebSocketHashMap().get(friendID);
        return ObjectUtil.isNotNull(server);
    }

    //保存至Mongodb
    private boolean saveMessageTOMongo(MessageDTO messageDTO, String uuid, Date date) {
        //默认消息未读;
        MonGoMessage message = new MonGoMessage();
        BeanUtils.copyProperties(messageDTO, message);
        message.setSendTime(date).setMessageID(uuid).setReadStatus(0);
        return monGoMessageDao.save(message);
    }

    //保存至Mysql;
    private boolean saveMessageTOMySql(MessageDTO messageDTO, String uuid, Date date) {
        //默认消息未读;
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        message.setSendTime(date).setMessageID(uuid).setReadStatus(0);
        int inserted = messageMapper.insert(message);
        return inserted > 0;
    }

    private String genMessageUUID() {
        return MESSAGE_HISTORY_UUID_SALT + IdUtil.fastUUID();
    }

    //暂时先保存至Redis;使用list(可重复列表 因为会有内容一模一样的消息)
    private boolean saveMessageToCache(MessageDTO messageDTO, String uuid, Date date) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);
        message.setSendTime(date).setMessageID(uuid).setReadStatus(0);
        String dr = messageDTO.getMasterID() + ":" + messageDTO.getFriendID();
        Long pushed = redisTemplate.boundListOps(MESSAGE_HISTORY_CACHE_PREFIX + dr)
                .leftPush(JSON.toJSONString(message));
        return pushed != null && pushed > 0;
    }

    //mq生产消息到队列;
    private void sendMessageToQueue(MessageDTO messageDTO, boolean friendOnlineStatus) {
        if (friendOnlineStatus) {
            log.info("好友已建立WS连接,消息不进入死信");
            //直接发送;
            rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.ROUTING_KEY, JSON.toJSONString(messageDTO));
        } else {
            log.info("好友未建立WS连接,消息进入死信");
            //直接发送至死信队列;
            rabbitTemplate.convertAndSend(RabbitMQConfig.DEAD_LETTER_QUEUE, JSON.toJSONString(messageDTO));
        }
    }

    public Response<Boolean> sendMessageToFriend(MessageDTO messageDTO) {
        boolean haveLogin = this.checkMasterHaveLogin(messageDTO.getMasterID());
        if (!haveLogin) {
            log.error("当前用户未登录");
            return Response.fail("当前用户未登录");
        }
        boolean currentUserOnlineStatus = this.getCurrentUserOnlineStatus(messageDTO.getMasterID());
        if (!currentUserOnlineStatus) {
            log.error("用户未建立WS连接");
            return Response.fail("用户未建立WS连接");
        }
        //好友存在且对方确实为你的好友;
        boolean userExists = this.userExists(messageDTO.getFriendID());
        if (!userExists) {
            log.error("发送目标不存在");
            return Response.fail("发送目标不存在");
        }
        boolean checkFriend = this.checkFriend(messageDTO.getMasterID(), messageDTO.getFriendID());
        if (!checkFriend) {
            log.warn("非好友");
            return Response.fail("非好友");
        }
        String uuid = this.genMessageUUID();
        Date date = new Date();
        CompletableFuture<Boolean> cacheCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return this.saveMessageToCache(messageDTO, uuid, date);
        });
        //保存至MongoDB;
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            boolean saveMessageTOMongo = saveMessageTOMongo(messageDTO, uuid, date);
            log.info("是否成功保存至Mongodb:{}", saveMessageTOMongo);
        });
        //保存至Mysql;
        CompletableFuture<Void> mysqlFuture = CompletableFuture.runAsync(() -> {
            boolean saved = this.saveMessageTOMySql(messageDTO, uuid, date);
            log.info("是否成功保存至Mysql:{}", saved);
        });
        mysqlFuture.join();
        future.join();
        Boolean res = cacheCompletableFuture.join();
        //获取对方在线状态
        boolean onlineStatus = this.getFriendOnlineStatus(messageDTO.getFriendID()); //假设为不在线 所以需要用上死信队列;
        this.sendMessageToQueue(messageDTO, onlineStatus);

        return Response.success(res);
    }
}
