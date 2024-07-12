package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.SysLog;
import com.bjpowernode.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/log")
@RequiredArgsConstructor
public class SysLogController {
    private final SysLogService sysLogService;

    @GetMapping("page")
    public ResponseEntity<Page<SysLog>> page(@RequestParam int current,
                                             @RequestParam int size){
        return ResponseEntity.ok(
                sysLogService.page(new Page<SysLog>(current, size))
        );
    }

}
