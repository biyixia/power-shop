package com.bjpowernode.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("message-service")
public interface MessageFeign {

    @GetMapping("/p/sms/sendWx")
    public ResponseEntity<String> sendWx(@RequestParam("userId") String userId);
}
