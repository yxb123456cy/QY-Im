package com.qy.im.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        /*String userId = (String) StpUtil.getLoginId();
        ServerHttpRequest httpRequest = request.mutate().headers(h -> h.add("userId", userId)).build();
        log.info("将userId传递到下游微服务:{}", userId);
        exchange.mutate().request(httpRequest); // 在auth微服务中测试是否能获取到userId;*/
        //放行;
        System.out.println(request.getCookies());
        System.out.println(request.getPath());
        System.out.println(request.getLocalAddress());
        System.out.println(request.getURI());
        System.out.println(request.getMethod());
        System.out.println(request.getId());
        System.out.println(request.getSslInfo());
        log.info("网关放行");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
