package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.OrderItem;
import com.bjpowernode.service.OrderItemService;
import com.bjpowernode.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【order_item(订单项)】的数据库操作Service实现
* @createDate 2024-03-17 22:24:08
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




