package com.bjpowernode.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
//@ApiModel("店铺订单对象")
public class ShopOrder {
    //@ApiModelProperty("商品条目集合")
    private List<OrderItem> shopCartItemDiscounts;
    //@ApiModelProperty("店铺满减")
    private BigDecimal shopReduce;
    //@ApiModelProperty("店铺运费")
    private BigDecimal transfee;
}
