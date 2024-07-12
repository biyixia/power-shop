package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.HotSearch;
import com.bjpowernode.service.HotSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotSearch")
@RequiredArgsConstructor
public class AdminHotSearchController {
    private final HotSearchService hotSearchService;

    @GetMapping("page")
    public ResponseEntity<Page<HotSearch>> page(int current, int size) {
        return ResponseEntity.ok(
                hotSearchService.page(new Page<>(current, size))
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addHostSearch(@RequestBody HotSearch hotSearch) {
        return ResponseEntity.ok(
                hotSearchService.save(hotSearch)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editHotSearch(@RequestBody HotSearch hotSearch) {
        return ResponseEntity.ok(
                hotSearchService.updateById(hotSearch)
        );
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delHotSearch(@RequestBody List<Long> hotSearchIds) {
        return ResponseEntity.ok(
                hotSearchService.removeByIds(hotSearchIds)
        );
    }
}
