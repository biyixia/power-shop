package com.bjpowernode.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("product-service")
public interface ProdFeign {

    @GetMapping("/prod/prod/getProdByIds")
    public List<Prod> getProdByIds(@RequestParam("prodIdList") List<Long> prodIdList);
}
