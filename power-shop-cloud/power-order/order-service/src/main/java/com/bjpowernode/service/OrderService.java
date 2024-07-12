package com.bjpowernode.service;

import com.bjpowernode.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.domain.OrderConfirm;
import com.bjpowernode.domain.OrderVo;

/**
* @author Administrator
* @description 针对表【order(订单表)】的数据库操作Service
* @createDate 2024-03-17 22:24:08
*/
public interface OrderService extends IService<Order> {

    public OrderVo confirm(OrderConfirm orderConfirm);

    public String submit(OrderVo orderVo);
}
