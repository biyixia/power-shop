package com.bjpowernode.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.domain.Basket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("cart-service")
public interface CartFeign {
    @PostMapping("/p/shopCart/clearBasket")
    boolean clearBasket(@RequestBody List<Long> skuIdList, @RequestParam("userId") String userId);
    @GetMapping("/p/shopCart/getBasketByIds")
    List<Basket> getBasketByIds(@RequestParam("basketIds") List<Long> basketIds);
    @PostMapping("/p/shopCart/saveBatchCarts")
    boolean saveBatchCarts(@RequestBody List<Basket> baskets);
}
