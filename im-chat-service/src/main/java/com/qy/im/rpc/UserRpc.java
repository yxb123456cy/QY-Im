package com.qy.im.rpc;

import com.qy.im.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "im-user-service")
public interface UserRpc {


    @PostMapping("/user/userExists")
    Response<Boolean> userExists(@RequestParam("userID") Long userId);

    @PostMapping("/user/checkFriend")
    Response<Boolean> checkFriend(@RequestParam("userID") Long masterID, @RequestParam("userID") Long userID);
}
