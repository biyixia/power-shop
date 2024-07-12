package com.bjpowernode.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.consts.GatewayConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class LoginRoute {

    private final StringRedisTemplate redisTemplate;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(
                        GatewayConstants.LOGIN_ROUTE_ID,
                        predicateSpec -> predicateSpec
                                .path(GatewayConstants.WHITE_PATH.get(0))
                                .filters(
                                        filter -> filter.modifyResponseBody(
                                                String.class,
                                                String.class,
                                                "application/json;charset=utf-8",
                                                ((serverWebExchange, responseBody) -> {
                                                    JSONObject jsonObject = JSON.parseObject(responseBody);
                                                    if (jsonObject.containsKey(GatewayConstants.TOKEN_PREFIX) && jsonObject.containsKey(GatewayConstants.TOKEN_EXPRESS_IN)) {
                                                        //获取token令牌和过期时间
                                                        String token = jsonObject.getString(GatewayConstants.TOKEN_PREFIX);
                                                        long expressIn = jsonObject.getLongValue(GatewayConstants.TOKEN_EXPRESS_IN);
                                                        //将token令牌和过期时间存入redis
                                                        redisTemplate.opsForValue().set(GatewayConstants.REDIS_TOKEN_PREFIX, token, expressIn, TimeUnit.SECONDS);
                                                    }
                                                    //返回响应体数据
                                                    return Mono.just(responseBody);
                                                })
                                        )
                                )
                                .uri(GatewayConstants.LB_AUTH_SERVER)
                )
                .build();
    }
}
