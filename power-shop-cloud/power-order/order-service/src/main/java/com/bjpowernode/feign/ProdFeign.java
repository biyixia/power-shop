package com.bjpowernode.feign;

import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.ProdSkuCount;
import com.bjpowernode.domain.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("product-service")
public interface ProdFeign {

    @GetMapping("/prod/prod/getSkuById")
    public Sku getSkuById(@RequestParam("skuId") Long skuId);

    @GetMapping("/prod/prod/getProdById")
    public Prod getProdById(@RequestParam("prodId") Long prodId);

    @GetMapping("/prod/prod/getProdByIds")
    public List<Prod> getProdByIds(@RequestParam("prodIdList") List<Long> prodIdList);

    @PutMapping("/prod/prod/deductMySQLStock")
    public boolean deductMySQLStock(@RequestBody List<ProdSkuCount> prodSkuCounts);
}
