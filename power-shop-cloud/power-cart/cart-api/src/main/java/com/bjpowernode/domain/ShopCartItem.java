package com.bjpowernode.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class ShopCartItem {
    //购物车id
    private Long basketId;
   //该条目是否被选择 
    private Boolean checked;
   //该条目对应的商品id 
    private Long prodId;
   //该条目对应的商品的名称 
    private String prodName;
   //该条目SkuId 
    private Long skuId;
   //该条目的名称 
    private String skuName;
   //该条目对应的主图 
    private String pic;
   //该条目对应的价格 
    private String price;
   //该条目对应数量 
    private Integer basketCount;
}
