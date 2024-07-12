package com.bjpowernode.controller;

import com.alibaba.fastjson.JSON;
import com.bjpowernode.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/p/order")
@RequiredArgsConstructor
public class PayController {
    private final PayService payService;

    @PostMapping("pay")
    //@RequestBody Map<String, Object> args
    public ResponseEntity<String> createPayCode() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("orderNumber", "1773189839043166208");
        return ResponseEntity.ok(
                payService.toPay(arguments)
        );
    }
}
