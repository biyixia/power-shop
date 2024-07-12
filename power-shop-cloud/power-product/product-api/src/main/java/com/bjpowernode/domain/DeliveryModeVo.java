package com.bjpowernode.domain;

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
public class DeliveryModeVo {
    private boolean hasShopDelivery;
    private boolean hasUserPickUp;
}
