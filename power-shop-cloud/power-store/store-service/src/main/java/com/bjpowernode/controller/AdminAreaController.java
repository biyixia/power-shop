package com.bjpowernode.controller;

import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.domain.Area;
import com.bjpowernode.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/area")
@RequiredArgsConstructor
public class AdminAreaController {
    private final AreaService areaService;

    @GetMapping("list")
    public ResponseEntity<List<Area>> list() {
        return ResponseEntity.ok(
                areaService.list()
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addArea(@RequestBody Area area) {
        return ResponseEntity.ok(
                areaService.save(area)
        );
    }

    @GetMapping("info/{areaId}")
    public ResponseEntity<Area> info(@PathVariable Long areaId) {
        return ResponseEntity.ok(
                areaService.getById(areaId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editArea(@RequestBody Area area) {
        return ResponseEntity.ok(
                areaService.updateById(area)
        );
    }

    @DeleteMapping("{areaId}")
    public ResponseEntity<Boolean> delArea(@PathVariable Long areaId) {
        return ResponseEntity.ok(
                areaService.removeById(areaId)
        );
    }

    @GetMapping("listByPid")
    public ResponseEntity<List<Area>> listByPid(Long pid) {
        return ResponseEntity.ok(
                areaService.list(
                        new LambdaQueryWrapper<Area>()
                                .eq(Area::getParentId, pid)
                )
        );
    }
}
