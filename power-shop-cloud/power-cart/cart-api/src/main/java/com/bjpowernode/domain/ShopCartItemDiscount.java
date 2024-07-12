package com.bjpowernode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class ShopCartItemDiscount {
    private Long shopId;
    private BigDecimal totalMoney = BigDecimal.ZERO;
    private BigDecimal finalMoney = BigDecimal.ZERO;
    private BigDecimal subtractMoney = BigDecimal.ZERO;
}
