package com.bjpowernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.ProdSkuCount;
import com.bjpowernode.domain.Sku;
import com.bjpowernode.service.ProdService;
import com.bjpowernode.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prod/prod")
@RequiredArgsConstructor
public class ProdController {
    private final ProdService prodService;
    private final SkuService skuService;

    @GetMapping("page")
    public ResponseEntity<Page<Prod>> page(int current, int size) {
        return ResponseEntity.ok(
                prodService.page(new Page<>(current, size))
        );
    }

    @GetMapping("prod/prodInfo")
    public ResponseEntity<Prod> prodInfo(Long prodId) {
        return ResponseEntity.ok(
                prodService.getById(prodId)
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addProd(@RequestBody Prod prod) {
        return ResponseEntity.ok(
                prodService.save(prod)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editProd(@RequestBody Prod prod) {
        return ResponseEntity.ok(
                prodService.updateById(prod)
        );
    }

    @GetMapping("info/{prodId}")
    public ResponseEntity<Prod> info(@PathVariable Long prodId) {
        return ResponseEntity.ok(
                prodService.getById(prodId)
        );
    }

    @DeleteMapping("{prodId}")
    public ResponseEntity<Boolean> delProd(@PathVariable Long prodId) {
        return ResponseEntity.ok(
                prodService.removeById(prodId)
        );
    }

    //------------------------------------------远程调用--------------------------------------------------
    @GetMapping("getProdByIds")
    public List<Prod> getProdByIds(@RequestParam("prodIdList") List<Long> prodIdList) {
        return prodService.listByIds(prodIdList);
    }

    @GetMapping("getProdById")
    public Prod getProdById(@RequestParam("prodId") Long prodId) {
        return prodService.getById(prodId);
    }


    @GetMapping("getSkuById")
    public Sku getSkuById(@RequestParam("skuId") Long skuId) {
        return skuService.getById(skuId);
    }

    @PutMapping("deductMySQLStock")
    public boolean deductMySQLStock(@RequestBody List<ProdSkuCount> prodSkuCounts) {
        return prodService.deductMySQLStock(prodSkuCounts);
    }
}
