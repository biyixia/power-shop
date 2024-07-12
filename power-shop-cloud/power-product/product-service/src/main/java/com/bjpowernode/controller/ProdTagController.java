package com.bjpowernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.ProdComm;
import com.bjpowernode.domain.ProdTag;
import com.bjpowernode.domain.SysUser;
import com.bjpowernode.feign.ManagerFeign;
import com.bjpowernode.service.ProdTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prod/prodTag")
@RequiredArgsConstructor
public class ProdTagController {
    private final ProdTagService prodTagService;
    private final ManagerFeign managerFeign;

    @GetMapping("page")
    public ResponseEntity<Page<ProdTag>> page(int current, int size) {
        return ResponseEntity.ok(
                prodTagService.page(new Page<>(current, size))
        );
    }

    @PostMapping
    private ResponseEntity<Boolean> addProdTag(@RequestBody ProdTag prodTag) {
        return ResponseEntity.ok(
                prodTagService.save(prodTag)
        );
    }

    @GetMapping("info/{prodTagId}")
    public ResponseEntity<ProdTag> info(@PathVariable Long prodTagId) {
        return ResponseEntity.ok(
                prodTagService.getById(prodTagId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editProdTag(@RequestBody ProdTag prodTag) {
        return ResponseEntity.ok(
                prodTagService.updateById(prodTag)
        );
    }

    @DeleteMapping("{prodTagId}")
    public ResponseEntity<Boolean> delProdTag(@PathVariable Long prodTagId) {
        return ResponseEntity.ok(
                prodTagService.removeById(prodTagId)
        );
    }

    @GetMapping("prodTagList")
    public ResponseEntity<List<ProdTag>> getProdTagList() {
        return ResponseEntity.ok(
                prodTagService.list()
        );
    }

    @GetMapping("listTagList")
    public ResponseEntity<List<ProdTag>> listTagList(){
        SysUser sysUser = managerFeign.getById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
        return ResponseEntity.ok(
                prodTagService.list(
                        new LambdaQueryWrapper<ProdTag>()
                                .eq(ProdTag::getShopId, sysUser.getShopId())
                                .eq(ProdTag::getStatus, 1)
                )
        );
    }

}

