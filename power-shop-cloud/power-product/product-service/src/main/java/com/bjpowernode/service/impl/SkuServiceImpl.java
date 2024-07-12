package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Sku;
import com.bjpowernode.service.SkuService;
import com.bjpowernode.mapper.SkuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sku(单品SKU表)】的数据库操作Service实现
* @createDate 2024-03-17 18:51:41
*/
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku>
    implements SkuService{
    @Override
    public boolean saveBatch(List<Sku> skuList) {
        for (Sku sku : skuList) {
            sku.setActualStocks(sku.getStocks());
            sku.setStocks(0);
            sku.setRecTime(new DateTime());
            sku.setUpdateTime(new DateTime());
            sku.setIsDelete(0);
            sku.setPartyCode("");
            sku.setModelId("");
        }
        return super.saveBatch(skuList);
    }
}




