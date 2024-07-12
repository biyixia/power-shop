package com.bjpowernode.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.consts.OrderConstants;
import com.bjpowernode.consts.QueueConstant;
import com.bjpowernode.domain.Basket;
import com.bjpowernode.domain.OrderItem;
import com.bjpowernode.domain.ProdSkuCount;
import com.bjpowernode.feign.CartFeign;
import com.bjpowernode.feign.ProdFeign;
import com.bjpowernode.service.OrderItemService;
import com.bjpowernode.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class OrderDeadQueueListener {
    private final OrderItemService orderItemService;
    private final CartFeign cartFeign;
    private final ProdFeign prodFeign;
    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    //@RabbitListener(queues = {QueueConstant.ORDER_DELAY_QUEUE})
    public void receiveOrderDelayQueue(Message message, Channel channel) {

    }
    @RabbitListener(queues = {QueueConstant.ORDER_DEAD_QUEUE})
    public void receiveOrderDeadQueue(Message message, Channel channel) {
        String json = new String(message.getBody());
        Map<String, String> map = JSON.parseObject(json, Map.class);
        String orderNumber = map.get(OrderConstants.ORDER_NUMBER_PREFIX);
        List<OrderItem> orderItemList = orderItemService.list(
                new LambdaQueryWrapper<OrderItem>()
                        .in(OrderItem::getOrderNumber, orderNumber)
        );
        //补回购物车数据
        List<OrderItem> orderItems = orderItemList.stream().filter(orderItem -> !ObjectUtils.isEmpty(orderItem.getBasketDate()))
                .collect(Collectors.toList());
        cartFeign.saveBatchCarts(
                orderItems.stream().map(orderItem -> {
                    return Basket.builder()
                            .shopId(orderItem.getShopId())
                            .prodId(orderItem.getProdId())
                            .skuId(orderItem.getSkuId())
                            .userId(orderItem.getUserId())
                            .basketCount(orderItem.getProdCount())
                            .basketDate(orderItem.getBasketDate())
                            //.discountId()
                            .distributionCardNo(orderItem.getDistributionCardNo())
                            .build();
                }).collect(Collectors.toList())
        );
        //补回es库存
        List<ProdSkuCount> prodSkuCountList = orderItemList.stream().map(orderItem -> {
            return ProdSkuCount.builder()
                    .prodId(orderItem.getProdId())
                    .skuId(orderItem.getSkuId())
                    .count(-orderItem.getProdCount())
                    .build();
        }).collect(Collectors.toList());
        rabbitTemplate.convertAndSend(
                QueueConstant.ES_CHANGE_QUEUE,
                JSON.toJSONString(prodSkuCountList)
        );
        //补回prod、sku库存

        prodFeign.deductMySQLStock(
                prodSkuCountList
        );
        try {
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag(),
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
