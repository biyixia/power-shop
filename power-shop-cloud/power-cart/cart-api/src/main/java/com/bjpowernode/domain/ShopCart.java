package com.bjpowernode.domain;

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
public class ShopCart {
    //@ApiModelProperty("店铺中商品条目的集合")
    private List<ShopCartItem> shopCartItems;
    // 优惠券  折扣
    //@ApiModelProperty("店铺的满减优惠")
    private BigDecimal shopReduce;
    //@ApiModelProperty("店铺中对应的运费")
    private BigDecimal yunfei;
}
