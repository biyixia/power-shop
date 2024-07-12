package com.bjpowernode.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.domain.Basket;
import com.bjpowernode.domain.ShopCart;
import com.bjpowernode.domain.ShopCartItemDiscount;
import com.bjpowernode.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/p/shopCart")
@RequiredArgsConstructor
public class PShopCartController {
    private final BasketService basketService;

    @GetMapping("prodCount")
    public ResponseEntity<Integer> prodCount() {
        return ResponseEntity.ok(
                basketService.count(
                        new LambdaQueryWrapper<Basket>()
                                .eq(Basket::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
                )
        );
    }

    @GetMapping("info")
    public ResponseEntity<List<ShopCart>> info() {
        return ResponseEntity.ok(
                basketService.info()
        );
    }

    @PostMapping("changeItem")
    public ResponseEntity<Boolean> changeItem(@RequestBody Basket basket) {
        return ResponseEntity.ok(
                basketService.saveBasket(basket)
        );
    }

    @PostMapping("totalPay")
    public ResponseEntity<ShopCartItemDiscount> totalPay(@RequestBody List<Long> basketIds) {
        return ResponseEntity.ok(
                basketService.getTotalPay(basketIds)
        );
    }

    @DeleteMapping("deleteItem")
    public ResponseEntity<Boolean> deleteItem(@RequestBody List<Long> basketIds) {
        return ResponseEntity.ok(
                basketService.removeByIds(basketIds)
        );
    }

    //------------------------------远程调用------------------------------------------
    @GetMapping("getBasketByIds")
    public List<Basket> getBasketByIds(@RequestParam("basketIds")List<Long> basketIds) {
        return basketService.listByIds(basketIds);
    }

    @PostMapping("clearBasket")
    boolean clearBasket(@RequestBody List<Long> skuIdList, @RequestParam("userId") String userId) {
        return basketService.remove(
                new LambdaQueryWrapper<Basket>()
                        .eq(Basket::getUserId, userId)
                        .in(Basket::getSkuId, skuIdList)
        );
    }

    @PostMapping("saveBatchCarts")
    boolean saveBatchCarts(@RequestBody List<Basket> baskets) {
        return basketService.saveBatch(baskets);
    }
}
