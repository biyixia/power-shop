package com.bjpowernode.feign;

import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("product-service")
public interface ProdFeign {

    @GetMapping("/prod/prod/getProdById")
    public Prod getProdById(@RequestParam("prodId") Long prodId);

    @GetMapping("/prod/prod/getSkuById")
    public Sku getSkuById(@RequestParam("skuId") Long skuId);
}
