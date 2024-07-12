package com.bjpowernode.controller;

import com.bjpowernode.domain.OrderConfirm;
import com.bjpowernode.domain.OrderVo;
import com.bjpowernode.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/p/myOrder")
@RequiredArgsConstructor
public class PMyOrderController {
    private final OrderService orderService;

    @PostMapping("confirm")
    public ResponseEntity<OrderVo> confirm(@RequestBody OrderConfirm orderConfirm) {
        return ResponseEntity.ok(
                orderService.confirm(orderConfirm)
        );
    }

    @PostMapping("submit")
    public ResponseEntity<String> submit(@RequestBody OrderVo orderVo) {
        return ResponseEntity.ok(
                "orderNumber:"+orderService.submit(orderVo)
        );
    }
}
