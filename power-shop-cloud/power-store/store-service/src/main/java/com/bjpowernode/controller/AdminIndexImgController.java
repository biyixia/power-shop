package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.IndexImg;
import com.bjpowernode.service.IndexImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/indexImg")
@RequiredArgsConstructor
public class AdminIndexImgController {
    private final IndexImgService indexImgService;

    @GetMapping("page")
    public ResponseEntity<Page<IndexImg>> page(int current, int size) {
        return ResponseEntity.ok(
                indexImgService.page(new Page<>(current, size))
        );
    }

    @GetMapping("indexImgs")
    public ResponseEntity<List<IndexImg>> getIndexImgs() {
        return ResponseEntity.ok(
                indexImgService.list()
        );
    }
}
