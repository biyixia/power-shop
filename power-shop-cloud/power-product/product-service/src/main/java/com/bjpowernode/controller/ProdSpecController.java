package com.bjpowernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.ProdProp;
import com.bjpowernode.domain.ProdPropValue;
import com.bjpowernode.domain.SysUser;
import com.bjpowernode.feign.ManagerFeign;
import com.bjpowernode.service.ProdPropService;
import com.bjpowernode.service.ProdPropValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prod/spec")
public class ProdSpecController {

    private final ProdPropService prodPropService;
    private final ManagerFeign managerFeign;
    private final ProdPropValueService prodPropValueService;

    @GetMapping("page")
    public ResponseEntity<Page<ProdProp>> page(int current, int size) {
        return ResponseEntity.ok(
                prodPropService.page(new Page<>(current, size))
        );
    }

    @GetMapping("list")
    public ResponseEntity<List<ProdProp>> list(){
        SysUser sysUser = managerFeign.getById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
        return ResponseEntity.ok(
                prodPropService.list(
                        new LambdaQueryWrapper<ProdProp>()
                                .eq(ProdProp::getShopId, sysUser.getShopId())
                )
        );
    }

    @PostMapping
    public ResponseEntity<Boolean> addSpec(@RequestBody ProdProp prodProp) {
        return ResponseEntity.ok(
                prodPropService.save(prodProp)
        );
    }

    @PutMapping
    public ResponseEntity<Boolean> editSpec(@RequestBody ProdProp prodProp) {
        return ResponseEntity.ok(
                prodPropService.updateById(prodProp)
        );
    }

    @DeleteMapping("{prodPropId}")
    public ResponseEntity<Boolean> delSpec(@PathVariable Long prodPropId) {
        return ResponseEntity.ok(
                prodPropService.removeById(prodPropId)
        );
    }

    @GetMapping("listSpecValue/{prodPropId}")
    public ResponseEntity<List<ProdPropValue>> listSpecValue(@PathVariable Long prodPropId) {
        return ResponseEntity.ok(
                prodPropValueService.list(
                        new LambdaQueryWrapper<ProdPropValue>()
                                .eq(ProdPropValue::getPropId, prodPropId)
                )
        );
    }

}
