package com.bjpowernode.service;

import com.bjpowernode.domain.Sku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sku(单品SKU表)】的数据库操作Service
* @createDate 2024-03-17 18:51:41
*/
public interface SkuService extends IService<Sku> {
    public boolean saveBatch(List<Sku> skuList);
}
