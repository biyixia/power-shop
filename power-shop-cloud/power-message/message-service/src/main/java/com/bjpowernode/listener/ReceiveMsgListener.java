package com.bjpowernode.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.consts.MessageConstants;
import com.bjpowernode.consts.QueueConstant;
import com.bjpowernode.domain.WxMsgTemplate;
import com.bjpowernode.properties.WxTemplateProperties;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class ReceiveMsgListener {
    private final WxTemplateProperties wxTemplateProperties;
    private final StringRedisTemplate redisTemplate;
    private final RestTemplate restTemplate;
    @RabbitListener(queues = QueueConstant.WX_MSG_QUEUE)
    public void receiveWxMsgQueue(Message message, Channel channel) {
        String json = new String(message.getBody());
        WxMsgTemplate wxMsgTemplate = JSON.parseObject(json, WxMsgTemplate.class);
        String accessToken = redisTemplate.opsForValue().get(MessageConstants.WX_MSG_TOKEN_PREFIX);
        String url = String.format(
                wxTemplateProperties.getTemplateUrl(),
                accessToken
        );
        String result = restTemplate.postForObject(
                url,
                wxMsgTemplate,
                String.class
        );
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")) {
            String errCode = jsonObject.getString("errcode");
            String errMsg = jsonObject.getString("errmsg");
            if ("0".equals(errCode)) {
                try {
                    channel.basicAck(
                            message.getMessageProperties().getDeliveryTag(),
                            false
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
