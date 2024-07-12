package com.bjpowernode.service;

import com.bjpowernode.domain.Basket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.domain.ShopCart;
import com.bjpowernode.domain.ShopCartItemDiscount;

import java.math.BigDecimal;
import java.util.List;

/**
* @author Administrator
* @description 针对表【basket(购物车)】的数据库操作Service
* @createDate 2024-03-17 22:11:56
*/
public interface BasketService extends IService<Basket> {

    public boolean saveBasket(Basket basket);

    public List<ShopCart> info();

    public ShopCartItemDiscount getTotalPay(List<Long> basketIds);
}
