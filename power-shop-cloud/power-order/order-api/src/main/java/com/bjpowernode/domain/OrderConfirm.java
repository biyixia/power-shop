package com.bjpowernode.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class OrderConfirm {
    private List<Long> basketIds;
    private OrderItem orderItem;
}
