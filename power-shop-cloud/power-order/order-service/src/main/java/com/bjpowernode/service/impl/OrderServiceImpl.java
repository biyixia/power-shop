package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.consts.QueueConstant;
import com.bjpowernode.domain.*;
import com.bjpowernode.feign.CartFeign;
import com.bjpowernode.feign.MemberFeign;
import com.bjpowernode.feign.MessageFeign;
import com.bjpowernode.feign.ProdFeign;
import com.bjpowernode.service.OrderItemService;
import com.bjpowernode.service.OrderService;
import com.bjpowernode.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【order(订单表)】的数据库操作Service实现
 * @createDate 2024-03-17 22:24:08
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {
    private final CartFeign cartFeign;
    private final MemberFeign memberFeign;
    private final ProdFeign prodFeign;
    private final Snowflake snowflake;
    private final OrderItemService orderItemService;
    private final RabbitTemplate rabbitTemplate;
    private final MessageFeign messageFeign;

    @Override
    public OrderVo confirm(OrderConfirm orderConfirm) {
        List<Long> basketIds = orderConfirm.getBasketIds();
        if (!CollectionUtils.isEmpty(basketIds)) {
            //购物车下单
            List<Basket> baskets = cartFeign.getBasketByIds(basketIds);
            //获取店铺集合
            ArrayList<Long> shopIdList = new ArrayList<>();
            for (Basket basket : baskets) {
                if (!shopIdList.contains(basket.getShopId())) {
                    shopIdList.add(basket.getShopId());
                }
            }
            List<ShopOrder> shopCartOrders = shopIdList.stream().map(shopId -> {
                List<Basket> basketList = baskets.stream().filter(
                        basket -> basket.getShopId().equals(shopId)
                ).collect(Collectors.toList());
                List<OrderItem> shopCartItemDiscounts = basketList.stream().map(basket -> {
                    Prod prod = prodFeign.getProdById(basket.getProdId());
                    Sku sku = prodFeign.getSkuById(basket.getSkuId());
                    return OrderItem.builder()
                            .shopId(shopId)
                            .prodId(basket.getProdId())
                            .skuId(basket.getSkuId())
                            .prodCount(basket.getBasketCount())
                            .prodName(prod.getProdName())
                            .skuName(sku.getSkuName())
                            .pic(prod.getPic())
                            .price(sku.getPrice())
                            .userId(SecurityContextHolder.getContext().getAuthentication().getName())
                            .productTotalAmount(sku.getPrice().multiply(BigDecimal.valueOf(basket.getBasketCount())))
                            .recTime(new DateTime())
                            .distributionCardNo("")
                            .basketDate(basket.getBasketDate())
                            .build();
                }).collect(Collectors.toList());
                BigDecimal transfee = BigDecimal.ZERO;
                BigDecimal shopReduce = BigDecimal.ZERO;
                return ShopOrder.builder()
                        .shopCartItemDiscounts(shopCartItemDiscounts)
                        .shopReduce(shopReduce)
                        .transfee(transfee)
                        .build();
            }).collect(Collectors.toList());
            //获取总数量
            Integer totalCount = baskets.stream().map(Basket::getBasketCount).collect(Collectors.toList())
                    .stream().reduce(Integer::sum).get();
            //获取总金额
            BigDecimal total = shopCartOrders.stream().map(shopOrder -> {
                        BigDecimal shopProdTotal = shopOrder.getShopCartItemDiscounts().stream().map(OrderItem::getProductTotalAmount)
                                .collect(Collectors.toList())
                                .stream().reduce(BigDecimal::add).get();
                        BigDecimal shopTransfeeTotal = shopOrder.getTransfee();
                        return shopProdTotal.add(shopTransfeeTotal);
                    }).collect(Collectors.toList())
                    .stream().reduce(BigDecimal::add).get();
            //获取商店优惠金额
            BigDecimal shopReduce = shopCartOrders.stream().map(ShopOrder::getShopReduce)
                    .collect(Collectors.toList())
                    .stream().reduce(BigDecimal::add).get();
            //获取运费金额
            //List<Prod> prodList = prodFeign.getProdByIds(baskets.stream().map(Basket::getProdId).collect(Collectors.toList()));
            //prodList.stream().map(Prod::getDeliveryTemplateId);
            BigDecimal transfee = BigDecimal.ZERO;
            //获取实际金额
            BigDecimal actualTotal = total.subtract(shopReduce);
            return OrderVo.builder()
                    .userAddr(memberFeign.getDefaultAddr(SecurityContextHolder.getContext().getAuthentication().getName()))
                    .shopCartOrders(shopCartOrders)
                    .totalCount(totalCount)
                    .actualTotal(actualTotal)
                    .total(total)
                    .shopReduce(shopReduce)
                    .transfee(transfee)
                    .remarks("")
                    .couponIds(null)
                    .build();
        } else {
            //商品详情页面下单
            OrderItem orderItem = orderConfirm.getOrderItem();
            return OrderVo.builder()
                    //.totalCount()
                    //.actualTotal()
                    //.total()
                    //.shopReduce()
                    //.transfee()
                    //.remarks()
                    //.couponIds()
                    .build();
        }
    }

    @Override
    public String submit(OrderVo orderVo) {
        //生成订单号
        String orderNumber = snowflake.nextIdStr();
        List<OrderItem> orderItemList = orderVo.getShopCartOrders()
                .stream().map(ShopOrder::getShopCartItemDiscounts).collect(Collectors.toList())
                .stream().flatMap(List::stream).collect(Collectors.toList());
        //保存订单项数据
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNumber(orderNumber);
        }
        orderItemService.saveBatch(orderItemList);
        List<Long> skuIds = orderItemList
                .stream().filter(orderItem -> !ObjectUtils.isEmpty(orderItem.getBasketDate())).collect(Collectors.toList())
                .stream().map(OrderItem::getSkuId).collect(Collectors.toList());
        //若是购物车下单，删除购物车中的数据
        clearCart(skuIds);
        List<ProdSkuCount> prodSkuCounts = orderItemList.stream().map(orderItem -> {
            return ProdSkuCount.builder()
                    .prodId(orderItem.getProdId())
                    .skuId(orderItem.getSkuId())
                    .count(orderItem.getProdCount())
                    .build();
        }).collect(Collectors.toList());
        //扣减es库存信息
        deductElasticSearchStock(prodSkuCounts);
        //扣减prod表、sku表中对应的库存
        prodFeign.deductMySQLStock(prodSkuCounts);
        //发送微信公众号消息
        sendWxMsg();
        //发送支付计时消息，若30分钟未支付，则进入死信队列执行补回操作
        sendPayMsg(orderNumber);
        return orderNumber;
    }

    private void sendPayMsg(String orderNumber) {
        rabbitTemplate.convertAndSend(
            QueueConstant.ORDER_DELAY_QUEUE,
                "{\"orderNumber\":\""+orderNumber+"\"}"
        );
    }

    private void sendWxMsg() {
        messageFeign.sendWx("o3ctD6VpWiCiojMF7-lbJs1zF-vA");
    }

    public void deductElasticSearchStock(List<ProdSkuCount> prodSkuCounts) {
        rabbitTemplate.convertAndSend(
                QueueConstant.ES_CHANGE_QUEUE,
                JSON.toJSONString(prodSkuCounts)
        );
    }

    private void clearCart(List<Long> skuIds) {
        cartFeign.clearBasket(
                skuIds,
                SecurityContextHolder.getContext().getAuthentication().getName()
        );
    }
}




