package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.*;
import com.bjpowernode.feign.ManagerFeign;
import com.bjpowernode.service.ProdService;
import com.bjpowernode.mapper.ProdMapper;
import com.bjpowernode.service.ProdTagReferenceService;
import com.bjpowernode.service.ProdTagService;
import com.bjpowernode.service.SkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【prod(商品)】的数据库操作Service实现
* @createDate 2024-03-17 18:51:41
*/
@Service
@RequiredArgsConstructor
public class ProdServiceImpl extends ServiceImpl<ProdMapper, Prod>
    implements ProdService{
    private final SkuService skuService;
    private final ManagerFeign managerFeign;
    private final ProdTagReferenceService prodTagReferenceService;
    private final ProdTagService prodTagService;
    @Override
    public Prod getById(Long prodId) {
        Prod prod = super.getById(prodId);
        List<Sku> skuList = skuService.list(
                new LambdaQueryWrapper<Sku>()
                        .eq(Sku::getProdId, prodId)
                        .eq(Sku::getStatus, 1)
        );
        List<ProdTagReference> prodTagReferences = prodTagReferenceService.list(
                new LambdaQueryWrapper<ProdTagReference>()
                        .eq(ProdTagReference::getProdId, prod.getProdId())
                        .eq(ProdTagReference::getShopId, prod.getShopId())
                        .eq(ProdTagReference::getStatus, 1)
        );
        if (!CollectionUtils.isEmpty(prodTagReferences)) {
            prod.setTagList(
                    prodTagReferences.stream().map(ProdTagReference::getTagId)
                            .collect(Collectors.toList())
            );
        }
        prod.setSkuList(skuList);
        prod.setDefaultSku(CollectionUtils.isEmpty(skuList)? null:skuList.get(0));
        return prod;
    }

    @Override
    public boolean deductMySQLStock(List<ProdSkuCount> prodSkuCounts) {
        ArrayList<Long> prodIdList = new ArrayList<>();
        //扣减sku表中的库存
        for (ProdSkuCount prodSkuCount : prodSkuCounts) {
            //获取不重复的prodId
            if (!prodIdList.contains(prodSkuCount.getProdId()))
                prodIdList.add(prodSkuCount.getProdId());
            Sku sku = skuService.getById(prodSkuCount.getSkuId());
            sku.setActualStocks(sku.getActualStocks() - prodSkuCount.getCount());
            sku.setStocks(sku.getStocks() + prodSkuCount.getCount());
            if (!skuService.updateById(sku)) {
                return false;
            }
        }
        //扣减prod表中的库存
        for (Long prodId : prodIdList) {
            Integer prodDeduct = prodSkuCounts.stream().filter(
                            prodSkuCount -> prodSkuCount.getProdId().equals(prodId))
                    .collect(Collectors.toList())
                    .stream().map(ProdSkuCount::getCount).collect(Collectors.toList())
                    .stream().reduce(Integer::sum).get();
            Prod prod = super.getById(prodId);
            if (!super.updateById(prod.setTotalStocks(prod.getTotalStocks() - prodDeduct))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean save(Prod prod) {
        SysUser sysUser = managerFeign.getById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
        prod.setShopId(sysUser.getShopId());
        prod.setCreateTime(new DateTime());
        prod.setSoldNum(0);
        prod.setUpdateTime(new DateTime());
        prod.setDeliveryMode(JSON.toJSONString(prod.getDeliveryModeVo()));
        prod.setVersion(0);
        //上架时间
        if (prod.getStatus().equals(1)) {
            prod.setPutawayTime(new DateTime());
        }
        boolean saveProd = super.save(prod);
        List<Sku> skuList = prod.getSkuList();
        for (Sku sku : skuList) {
            sku.setProdId(prod.getProdId());
        }
        boolean saveTag = prodTagReferenceService.saveBatch(
                prod.getTagList().stream().map(tagId -> {
                    return ProdTagReference.builder()
                            .shopId(sysUser.getShopId())
                            .tagId(tagId)
                            .prodId(prod.getProdId())
                            .status(1)
                            .createTime(new DateTime())
                            .build();
                }).collect(Collectors.toList())
        );
        return saveProd && skuService.saveBatch(skuList) && saveTag;
    }

    @Override
    public boolean updateById(Prod prod) {
        prod.setUpdateTime(new DateTime());
        prod.setDeliveryMode(JSON.toJSONString(prod.getDeliveryModeVo()));
        //设置上架时间
        if (prod.getStatus().equals(1) && this.getById(prod.getProdId()).getStatus().equals(0)) {
            prod.setPutawayTime(new DateTime());
        }
        super.updateById(prod);
        //修改标签引用子表
        prodTagReferenceService.remove(
                new LambdaQueryWrapper<ProdTagReference>()
                        .eq(ProdTagReference::getShopId, prod.getShopId())
                        .eq(ProdTagReference::getProdId, prod.getProdId())
                        .eq(ProdTagReference::getStatus, 1)
        );
        prodTagReferenceService.saveBatch(
                prod.getTagList().stream().map(tagId -> {
                    return ProdTagReference.builder()
                            .shopId(prod.getShopId())
                            .tagId(tagId)
                            .prodId(prod.getProdId())
                            .status(1)
                            .createTime(new DateTime())
                            .build();
                }).collect(Collectors.toList())
        );
        //修改sku子表
        skuService.remove(
                new LambdaQueryWrapper<Sku>()
                        .eq(Sku::getProdId, prod.getProdId())
                        .eq(Sku::getIsDelete, 0)
        );
        for (Sku sku : prod.getSkuList()) {
            sku.setProdId(prod.getProdId());
        }
        skuService.saveBatch(prod.getSkuList());
        return true;
    }

    @Override
    public boolean removeById(Long prodId) {
        Prod prod = this.getById(prodId);
        super.removeById(prodId);
        //删除tag引用表中的数据
        prodTagReferenceService.removeByIds(
                prodTagReferenceService.list(
                        new LambdaQueryWrapper<ProdTagReference>()
                                .eq(ProdTagReference::getShopId, prod.getShopId())
                                .eq(ProdTagReference::getProdId, prod.getProdId())
                                .eq(ProdTagReference::getStatus, 1)
                )
        );
        //删除sku表中的数据
        skuService.removeByIds(
                prod.getSkuList().stream().map(Sku::getSkuId)
                        .collect(Collectors.toList())
        );
        return true;
    }
}




