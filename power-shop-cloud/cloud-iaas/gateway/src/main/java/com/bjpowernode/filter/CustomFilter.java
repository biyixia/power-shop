package com.bjpowernode.filter;

import com.alibaba.fastjson.JSON;
import com.bjpowernode.consts.GatewayConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

//自定义过滤器CustomFilter
public class CustomFilter implements GlobalFilter, Ordered{
    //执行过滤器的业务逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //若是登录请求（/oauth/token）则放行，路由到认证中心颁发token
        String path = exchange.getRequest().getURI().getPath();
        if (GatewayConstants.WHITE_PATH.contains(path)) {
            return chain.filter(exchange);
        }
        //若是非登录请求，则看是否携带token令牌
        String token = exchange.getRequest().getHeaders().getFirst(GatewayConstants.AUTHORIZATION_HEADER);
        //若携带token令牌（具有对应的权限），则放行
        if (token.contains(GatewayConstants.BEARER_PREFIX_UPPER_CASE) || token.contains(GatewayConstants.BEARER_PREFIX_LOWER_CASE)) {
            return chain.filter(exchange);
        }
        //若未携带token令牌，则返回不具有相关权限的错误提示
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 20403);
        resultMap.put("msg", "未授权");
        exchange.getResponse().getHeaders().add(GatewayConstants.CONTENT_TYPE,GatewayConstants.APPLICATION_UTF_8);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(
                        JSON.toJSONBytes(resultMap)
                ))
        );
    }
    //执行过滤器的顺序，数字越小优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
