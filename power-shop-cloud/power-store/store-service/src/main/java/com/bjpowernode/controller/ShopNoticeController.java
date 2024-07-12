package com.bjpowernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Notice;
import com.bjpowernode.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/notice")
@RequiredArgsConstructor
public class ShopNoticeController {
    private final NoticeService noticeService;

    @GetMapping("page")
    public ResponseEntity<Page<Notice>> page(int current, int size) {
        return ResponseEntity.ok(
                noticeService.page(new Page<>(current, size))
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addNotice(@RequestBody Notice notice) {
        return ResponseEntity.ok(
                noticeService.save(notice)
        );
    }

    @GetMapping("info/{noticeId}")
    public ResponseEntity<Notice> info(@PathVariable Long noticeId) {
        return ResponseEntity.ok(
                noticeService.getById(noticeId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editNotice(@RequestBody Notice notice) {
        return ResponseEntity.ok(
                noticeService.updateById(notice)
        );
    }

    @DeleteMapping("{noticeId}")
    public ResponseEntity<Boolean> delNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(
                noticeService.removeById(noticeId)
        );
    }

    @GetMapping("topNoticeList")
    public ResponseEntity<List<Notice>> getTopNoticeList() {
        return ResponseEntity.ok(
                noticeService.list(
                        new LambdaQueryWrapper<Notice>()
                                .eq(Notice::getStatus, 1)
                                .eq(Notice::getIsTop, 1)
                )
        );
    }

}
