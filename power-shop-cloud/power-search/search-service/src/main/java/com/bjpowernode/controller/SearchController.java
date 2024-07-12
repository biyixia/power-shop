package com.bjpowernode.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.ProdEs;
import com.bjpowernode.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    ///根据商品名称模糊分页查询
    @GetMapping("/search/searchProdPage")
    public ResponseEntity<Page<ProdEs>> searchProdPage(int current, int size, String prodName, int sort) {
        return ResponseEntity.ok(
                searchService.searchProdPage(current, size, prodName, sort)
        );
    }

    @GetMapping("/prod/prodListByTagId")
    public ResponseEntity<Page<ProdEs>> prodListByTagId(Long tagId, String size) {
        return ResponseEntity.ok(
                searchService.prodListByTagId(tagId, size)
        );
    }

    @GetMapping("/prod/pageProd")
    public ResponseEntity<Page<ProdEs>> pageProd(@RequestParam("cateGoryId") Long categoryId) {
        return ResponseEntity.ok(
                searchService.pageProd(categoryId)
        );
    }
}
