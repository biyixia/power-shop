package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.*;
import com.bjpowernode.exception.AppException;
import com.bjpowernode.feign.ProdFeign;
import com.bjpowernode.service.BasketService;
import com.bjpowernode.mapper.BasketMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【basket(购物车)】的数据库操作Service实现
* @createDate 2024-03-17 22:11:56
*/
@Service
@RequiredArgsConstructor
public class BasketServiceImpl extends ServiceImpl<BasketMapper, Basket>
    implements BasketService{
    private final ProdFeign prodFeign;
    @Override
    public boolean saveBasket(Basket basket) {
        Basket indexBasket = super.getOne(
                new LambdaQueryWrapper<Basket>()
                        .eq(Basket::getProdId, basket.getProdId())
                        .eq(Basket::getShopId, basket.getShopId())
                        .eq(Basket::getSkuId, basket.getSkuId())
                        .eq(Basket::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
        );
        if (!ObjectUtils.isEmpty(indexBasket)) {
            int finalBasketCount = indexBasket.getBasketCount() + basket.getBasketCount();
            Sku sku = prodFeign.getSkuById(indexBasket.getSkuId());
            if (finalBasketCount <= 0) {
                throw new AppException("宝贝无法再减少了哦~");
            }
            if (finalBasketCount > sku.getActualStocks()) {
                throw new AppException("再添加宝贝的库存就不足啦~");
            }
            return super.updateById(
                    indexBasket.setBasketCount(indexBasket.getBasketCount() + basket.getBasketCount())
            );
        }
        basket.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        basket.setBasketDate(new DateTime());
        return super.save(basket);
    }

    @Override
    public List<ShopCart> info() {
        List<Basket> basketList = super.list(
                new LambdaQueryWrapper<Basket>()
                        .eq(Basket::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
        );
        List<Long> shopIdList = new ArrayList<>();
        for (Basket basket : basketList) {
            if (!shopIdList.contains(basket.getShopId())) {
                shopIdList.add(basket.getShopId());
            }
        }
        List<ShopCart> shopCarts = new ArrayList<>();
        for (Long shopId : shopIdList) {
            List<Basket> collect = basketList.stream().filter(basket -> shopId.equals(basket.getShopId())).collect(Collectors.toList());
            List<ShopCartItem> shopCartItems = collect.stream().map(basket -> {
                Prod prod = prodFeign.getProdById(basket.getProdId());
                Sku sku = prodFeign.getSkuById(basket.getSkuId());
                return ShopCartItem.builder()
                        .basketId(basket.getBasketId())
                        .prodId(basket.getProdId())
                        .prodName(prod.getProdName())
                        .skuId(basket.getSkuId())
                        .skuName(sku.getSkuName())
                        .pic(sku.getPic())
                        .price(String.valueOf(sku.getPrice()))
                        .basketCount(basket.getBasketCount())
                        .build();
            }).collect(Collectors.toList());
            shopCarts.add(
                    ShopCart.builder()
                            .shopCartItems(shopCartItems)
                            /// TODO: 2024/3/26
                            .shopReduce(BigDecimal.ZERO)
                            .yunfei(BigDecimal.ZERO)
                            .build()
            );
        }
        return shopCarts;
    }

    @Override
    public ShopCartItemDiscount getTotalPay(List<Long> basketIds) {
        if (basketIds.size() == 0) {
            return ShopCartItemDiscount.builder()
                    .totalMoney(BigDecimal.ZERO)
                    .subtractMoney(BigDecimal.ZERO)
                    .finalMoney(BigDecimal.ZERO)
                    .build();
        }
        List<Basket> baskets = super.listByIds(basketIds);
        BigDecimal totalMoney = baskets.stream().map(basket -> {
                    Sku sku = prodFeign.getSkuById(basket.getSkuId());
                    return sku.getPrice().multiply(BigDecimal.valueOf(basket.getBasketCount()));
                }).collect(Collectors.toList())
                .stream().reduce(BigDecimal::add).get();
        //discount
        BigDecimal subtractMoney = BigDecimal.ZERO;
        return ShopCartItemDiscount.builder()
                .totalMoney(totalMoney)
                .subtractMoney(subtractMoney)
                .finalMoney(totalMoney.subtract(subtractMoney))
                .build();
    }

}




