package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.PickAddr;
import com.bjpowernode.service.PickAddrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/pickAddr")
@RequiredArgsConstructor
public class ShopPickAddrController {
    private final PickAddrService pickAddrService;

    @GetMapping("page")
    public ResponseEntity<Page<PickAddr>> page(int current, int size) {
        return ResponseEntity.ok(
                pickAddrService.page(new Page<>(current, size))
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addPickAddr(@RequestBody PickAddr pickAddr) {
        return ResponseEntity.ok(
                pickAddrService.save(pickAddr)
        );
    }

    @GetMapping("info/{pickAddrId}")
    public ResponseEntity<PickAddr> info(@PathVariable Long pickAddrId) {
        return ResponseEntity.ok(
                pickAddrService.getById(pickAddrId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editPickAddr(@RequestBody PickAddr pickAddr) {
        return ResponseEntity.ok(
                pickAddrService.updateById(pickAddr)
        );
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delPickAddr(@RequestBody List<Long> pickAddrIds) {
        return ResponseEntity.ok(
                pickAddrService.removeByIds(pickAddrIds)
        );
    }
}
