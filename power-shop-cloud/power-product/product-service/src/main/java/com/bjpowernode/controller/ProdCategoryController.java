package com.bjpowernode.controller;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.domain.Category;
import com.bjpowernode.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/prod/category")
@RequiredArgsConstructor
public class ProdCategoryController {
    private final CategoryService categoryService;

    @GetMapping("table")
    public ResponseEntity<List<Category>> table() {
        return ResponseEntity.ok(
                categoryService.list()
        );
    }

    @GetMapping("listCategory")
    public ResponseEntity<List<Category>> listCategory() {
        return ResponseEntity.ok(
                categoryService.list()
        );
    }

    @GetMapping("info/{categoryId}")
    public ResponseEntity<Category> info(@PathVariable Long categoryId) {
        return ResponseEntity.ok(
                categoryService.getById(categoryId)
        );
    }

    @GetMapping("/category/categoryInfo")
    public ResponseEntity<List<Category>> getByParentId(String parentId) {
        return ResponseEntity.ok(
                categoryService.list(
                        new LambdaQueryWrapper<Category>()
                                .eq(StringUtils.isNotBlank(parentId), Category::getParentId, parentId)
                )
        );
    }
}
