package com.qy.im.ws;

import com.alibaba.fastjson2.JSON;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 后期需要改造为分布式 引入Redis;
 */
@Component
@Slf4j
@ServerEndpoint(value = "/chat/{userID}")
public class WebSocketServer {
    private static RedisTemplate<String, Object> redisTemplate;
    public static final String ONLINE_USERID_CACHE_PREFIX = "online_user_id";

    private static void addOnlineUserIDTOCache(Long userID) {
        redisTemplate.boundSetOps(ONLINE_USERID_CACHE_PREFIX)
                .add(userID);
    }

    //检测当前WS对话的userID是否重复;
    private static boolean checkWsUserId(Long userID) {
        return Boolean.TRUE.equals(redisTemplate.boundSetOps(ONLINE_USERID_CACHE_PREFIX)
                .isMember(userID));
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        WebSocketServer.redisTemplate = redisTemplate;
    }

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    public static int getOnlineCount() {
        return onlineCount.get();
    }

    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    public static void subOnlineCount() {
        onlineCount.getAndDecrement();
    }

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收userID
    @Getter
    @Setter
    private Long userID;

    /**
     * 存放每个客户端对应的WebSocket对象
     */
    public static CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<WebSocketServer>();
    public static ConcurrentHashMap<Long, WebSocketServer> webSocketServerConcurrentHashMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userID") Long userID) {
        boolean checkWsUserId = checkWsUserId(userID);
        if (!checkWsUserId) {
            this.session = session;
            setUserID(userID);//设置当前ws对象的userID;
            addOnlineUserIDTOCache(userID);
            addOnlineCount();//在线人数+1;
            webSockets.add(this);//webSockets set集合;
            webSocketServerConcurrentHashMap.put(userID, this);//加入至ConcurrentHashMap集合;
            log.info("有新用户加入");
            try {
                sendMessage("conn_success");//发给刚连接成功的ws客户端;
                log.info("有新客户端开始监听,userID={},当前在线人数为:{}", userID, getOnlineCount());
            } catch (IOException e) {
                log.error("websocket IO Exception:{}", e.getMessage());
            }
        } else {
            log.warn("当前ws对话已存在 userID:{}", userID);
        }
    }

    @OnClose
    public void onClose() throws IOException {
        webSockets.remove(this);//set中删除;
        webSocketServerConcurrentHashMap.remove(userID);//删除hashMap中的ws 对象;
        subOnlineCount();//在线人数减1;
        //Redis减一;
        redisTemplate.boundSetOps(ONLINE_USERID_CACHE_PREFIX)
                .remove(userID);
        log.info("有用户离开");
        log.info("释放userID={}的客户端", userID);
        releaseResource();
    }

    private void releaseResource() {
        // 这里写释放资源和要处理的业务
        log.info("有一连接关闭！当前在线人数为{}", getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String msg) throws InterruptedException {
        log.info("收到来自客户端 userID={} 的信息:{}", getUserID(), msg);
        // 群发消息
        HashSet<Long> userIds = new HashSet<>();
        for (WebSocketServer item : webSockets) {
            userIds.add(item.userID);
        }
        try {
            sendMessage("客户端 " + this.userID + "发布消息：" + msg, userIds);
        } catch (IOException e) {
            log.error("接收客户端信息发生异常:{}", e.getMessage());
        }
    }

    @OnError
    public void onError(Throwable error) {
        log.error("{}客户端发生错误", session.getBasicRemote());
        log.error("error:{}", error.getMessage());
    }

    /**
     * 群发自定义消息
     */
    public static void sendMessage(String message, HashSet<Long> toUserIDs) throws IOException {
        log.info("推送消息到客户端 {}，推送内容:{}", toUserIDs, message);
        for (WebSocketServer item : webSockets) {
            try {
                //这里可以设定只推送给传入的sid，为null则全部推送
                if (toUserIDs.isEmpty()) {
                    item.sendMessage(message);//空set 则群发;
                } else if (toUserIDs.contains(item.userID)) {
                    item.sendMessage(message);//单发给对应的Userid-websocket客户端;
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    //实现服务器主动推送消息到 指定客户端     //属于Websocket单个对象
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //属于Websocket单个对象
    public void sendAuditMsgToClient(String auditMessage) throws IOException {
        this.session.getBasicRemote().sendText(JSON.toJSONString(auditMessage));
    }

    // 获取当前在线客户端对应的WebSocket对象
    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSets() {
        return webSockets;
    }

    public static ConcurrentHashMap<Long, WebSocketServer> getWebSocketHashMap() {
        return webSocketServerConcurrentHashMap;
    }
}
