package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.SysConfig;
import com.bjpowernode.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/config")
@RequiredArgsConstructor
public class SysConfigController {
    private final SysConfigService sysConfigService;

    @GetMapping("page")
    public ResponseEntity<Page<SysConfig>> page(@RequestParam int current,
                                                @RequestParam int size){
        return ResponseEntity.ok(
                sysConfigService.page(new Page<SysConfig>(current, size))
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addSysConfig(@RequestBody SysConfig sysConfig) {
        return ResponseEntity.ok(
                sysConfigService.save(sysConfig)
        );
    }

    @GetMapping("info/{sysConfigId}")
    public ResponseEntity<SysConfig> info(@PathVariable Long sysConfigId) {
        return ResponseEntity.ok(
                sysConfigService.getById(sysConfigId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> updateSysConfig(@RequestBody SysConfig sysConfig) {
        return ResponseEntity.ok(
                sysConfigService.updateById(sysConfig)
        );
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteSysConfig(@RequestBody List<Long> sysConfigIds) {
        return ResponseEntity.ok(
                sysConfigService.removeByIds(sysConfigIds)
        );
    }
}
