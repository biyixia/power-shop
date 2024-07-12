package com.bjpowernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName transfee
 */
@TableName(value ="transfee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class Transfee implements Serializable {
    /**
     * 运费项id
     */
    @TableId(value = "transfee_id", type = IdType.AUTO)
    private Long transfeeId;

    /**
     * 运费模板id
     */
    @TableField(value = "transport_id")
    private Long transportId;

    /**
     * 续件数量
     */
    @TableField(value = "continuous_piece")
    private BigDecimal continuousPiece;

    /**
     * 首件数量
     */
    @TableField(value = "first_piece")
    private BigDecimal firstPiece;

    /**
     * 续件费用
     */
    @TableField(value = "continuous_fee")
    private BigDecimal continuousFee;

    /**
     * 首件费用
     */
    @TableField(value = "first_fee")
    private BigDecimal firstFee;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<Area> cityList;

    @TableField(exist = false)
    private Integer status;
}