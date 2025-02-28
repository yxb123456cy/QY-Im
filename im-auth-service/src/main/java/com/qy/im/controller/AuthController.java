package com.qy.im.controller;

import com.alibaba.druid.util.StringUtils;
import com.jthinking.common.util.ip.IPInfo;
import com.jthinking.common.util.ip.IPInfoUtils;
import com.qy.im.dtos.UserLoginDTO;
import com.qy.im.response.Response;
import com.qy.im.service.AuthService;
import com.qy.im.vos.UserLoginRespVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
 /* 1. 登录接口
    2. 认证接口
    3. 注销接口
    4. 第三方登录信息接口，用于在前端展示第三方登录
    5. 第三方登录接口*/

/**
 * Auth认证相关控制器;
 */
@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //获取客户端公网IP;
    @GetMapping("/getIpAddr")
    public  String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

    @GetMapping("/IP")
    public String getIpAddress(HttpServletRequest request) {
        //获取本机公网IP;
        try {
            URL MY_IP = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(MY_IP.openStream()));
            // 获取IP信息
            String IP = in.readLine();
            IPInfo ipInfo = IPInfoUtils.getIpInfo(IP);
            System.out.println(ipInfo.getCountry()); // 国家中文名称
            System.out.println(ipInfo.getProvince()); // 中国省份中文名称
            System.out.println(ipInfo.getAddress()); // 详细地址
            System.out.println(ipInfo.getIsp()); // 互联网服务提供商
            System.out.println(ipInfo.isOverseas()); // 是否是国外
            System.out.println(ipInfo.getLat()); // 纬度
            System.out.println(ipInfo.getLng()); // 经度
            return IP;
        } catch (Exception e) {
            log.error(e.toString());
        }
        return "";

    }

    @PostMapping("/login")
    @CrossOrigin("*")
    public Response<UserLoginRespVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        return authService.login(userLoginDTO);
    }

    //注销
    @PostMapping("/logout")
    @CrossOrigin("*")
    public Response<Boolean> logout() {
        return authService.logout();
    }

    //认证;
    @PostMapping("/authCheck")
    @CrossOrigin("*")
    public Response<UserLoginRespVO> authCheck() {
        return authService.authCheck();
    }

}
