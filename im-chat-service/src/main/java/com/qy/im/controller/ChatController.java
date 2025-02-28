package com.qy.im.controller;

import com.qy.im.dtos.MessageDTO;
import com.qy.im.response.Response;
import com.qy.im.service.ChatService;
import com.qy.im.ws.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {
    private final RedisTemplate<String, Object> redisTemplate;
    //从Redis中获取在线人数;
    private final ChatService service;
    @PostMapping("/sendMessage")
    public Response<Boolean> sendMessageToFriend(@RequestBody MessageDTO messageDTO){
        return service.sendMessageToFriend(messageDTO);
    }

    @GetMapping("/getOnlineUserCount")
    public Response<Long> getOnlineUserCount() {
        Long count = redisTemplate.boundSetOps(WebSocketServer.ONLINE_USERID_CACHE_PREFIX).size();
        return Response.success(count);
    }

    @GetMapping("/getWebSockets")
    public Response<?> getWebSockets() {
        CopyOnWriteArraySet<WebSocketServer> webSocketSets = WebSocketServer.getWebSocketSets();
        return Response.success(webSocketSets);
    }

    @GetMapping("/getOnlineWebSocketHashMap")
    public Response<?> getOnlineWebSocketHashMap() {
        ConcurrentHashMap<Long, WebSocketServer> webSocketHashMap = WebSocketServer.getWebSocketHashMap();
        return Response.success(webSocketHashMap);
    }
}
