package com.bjpowernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.SysUser;
import com.bjpowernode.domain.Transport;
import com.bjpowernode.feign.ManagerFeign;
import com.bjpowernode.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/transport")
@RequiredArgsConstructor
public class ShopTransportController {
    private final TransportService transportService;
    private final ManagerFeign managerFeign;

    @GetMapping("page")
    public ResponseEntity<Page<Transport>> page(int current, int size) {
        return ResponseEntity.ok(
                transportService.page(new Page<>(current, size))
        );
    }

    @GetMapping("list")
    public ResponseEntity<List<Transport>> list() {
        SysUser sysUser = managerFeign.getById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
        return ResponseEntity.ok(
                transportService.list(
                        new LambdaQueryWrapper<Transport>()
                                .eq(Transport::getShopId, sysUser.getShopId())
                )
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addTransport(@RequestBody Transport transport) {
        return ResponseEntity.ok(
                transportService.save(transport)
        );
    }

    @GetMapping("info/{transportId}")
    public ResponseEntity<Transport> info(@PathVariable Long transportId) {
        return ResponseEntity.ok(
                transportService.getById(transportId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editTransport(@RequestBody Transport transport) {
        return ResponseEntity.ok(
                transportService.updateById(transport)
        );
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delTransport(@RequestBody List<Long> transportIds) {
        return ResponseEntity.ok(
                transportService.removeByIds(transportIds)
        );
    }
}
