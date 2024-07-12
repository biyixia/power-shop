package com.bjpowernode.controller;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.consts.QueueConstant;
import com.bjpowernode.domain.WxMsgTemplate;
import com.bjpowernode.properties.WxMsgProperties;
import com.bjpowernode.properties.WxTemplateProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/p/sms")
@RequiredArgsConstructor
public class MessageController {
    private final RabbitTemplate rabbitTemplate;
    private final WxTemplateProperties wxTemplateProperties;
    @GetMapping("sendWx")
    public ResponseEntity<String> sendWx(@RequestParam("userId") String userId) {
        WxMsgTemplate wxMsgTemplate = WxMsgTemplate.builder()
                .touser(userId)
                .template_id(wxTemplateProperties.getTemplateId())
                .topcolor("#FF0000")
                .build();
        wxMsgTemplate.setData("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        wxMsgTemplate.setData("goods", "汤圆肥虫");
        wxMsgTemplate.setData("price", "10亿");
        wxMsgTemplate.setData("money", "-1万亿");
        rabbitTemplate.convertAndSend(
                QueueConstant.WX_MSG_QUEUE,
                JSONObject.toJSONString(wxMsgTemplate)
        );
        return ResponseEntity.ok(
                "ok"
        );
    }
}
