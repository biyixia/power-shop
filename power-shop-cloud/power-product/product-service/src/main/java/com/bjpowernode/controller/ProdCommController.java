package com.bjpowernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.ProdComm;
import com.bjpowernode.service.ProdCommService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prod/prodComm")
@RequiredArgsConstructor
public class ProdCommController {
    private final ProdCommService prodCommService;

    @GetMapping("page")
    public ResponseEntity<Page<ProdComm>> page(int current, int size) {
        return ResponseEntity.ok(
                prodCommService.page(new Page<>(current, size))
        );
    }

    @GetMapping("{prodCommId}")
    public ResponseEntity<ProdComm> getProdComm(@PathVariable Long prodCommId) {
        return ResponseEntity.ok(
                prodCommService.getById(prodCommId)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editProdComm(@RequestBody ProdComm prodComm) {
        return ResponseEntity.ok(
                prodCommService.updateById(prodComm)
        );
    }

    @GetMapping("prodComm/prodCommData")
    public ResponseEntity<List<ProdComm>> prodCommData(Long prodId) {
        return ResponseEntity.ok(
                prodCommService.list(
                        new LambdaQueryWrapper<ProdComm>()
                                .eq(ProdComm::getProdId, prodId)
                                .eq(ProdComm::getStatus, 1)
                )
        );
    }

    @GetMapping("prodComm/prodCommPageByProd")
    public ResponseEntity<Page<ProdComm>> prodCommPageByProd(Long prodId, int current, int size, int evaluate) {
        return ResponseEntity.ok(
                prodCommService.prodCommPageByProd(prodId, current, size, evaluate)
        );
    }
}
