package com.bjpowernode.load;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.bjpowernode.consts.MessageConstants;
import com.bjpowernode.properties.WxMsgProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WxAccessToken{
    private final RestTemplate restTemplate;
    private final WxMsgProperties wxMsgProperties;
    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void initWxToken(){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        if (StringUtils.isBlank(operations.get(MessageConstants.WX_MSG_TOKEN_PREFIX))) {
            getWxAccessToken();
        } else {
            Long expire = redisTemplate.getExpire(MessageConstants.WX_MSG_TOKEN_PREFIX, TimeUnit.SECONDS);
            if (expire!= null && expire.compareTo(300L) <= 0) {
                getWxAccessToken();
            }
        }
    }

    @Scheduled(initialDelay = 6900 * 1000, fixedDelay = 6900 * 1000)
    private void getWxAccessToken() {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String url = String.format(
                wxMsgProperties.getTokenUrl(),
                wxMsgProperties.getAppId(),
                wxMsgProperties.getAppSecret()
        );
        String json = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject.containsKey(MessageConstants.ACCESS_TOKEN_PREFIX) &&
                jsonObject.containsKey(MessageConstants.EXPIRES_IN_PREFIX))  {
            String accessToken = jsonObject.getString(MessageConstants.ACCESS_TOKEN_PREFIX);
            String expiresIn = jsonObject.getString(MessageConstants.EXPIRES_IN_PREFIX);
            operations.set(MessageConstants.WX_MSG_TOKEN_PREFIX, accessToken, Long.parseLong(expiresIn), TimeUnit.SECONDS);
        }
    }

}
