package com.qy.im.rpc;

import com.qy.im.dtos.UserLoginRespVO;
import com.qy.im.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "im-auth-service")
public interface AuthRpc {
    @PostMapping("/auth/authCheck")
    Response<UserLoginRespVO> checkUserHaveLogin(@RequestParam("userID") Long userID);
}
