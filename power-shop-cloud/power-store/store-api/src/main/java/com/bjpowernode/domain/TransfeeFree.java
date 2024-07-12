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
 * @TableName transfee_free
 */
@TableName(value ="transfee_free")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class TransfeeFree implements Serializable {
    /**
     * 指定条件包邮项id
     */
    @TableId(value = "transfee_free_id", type = IdType.AUTO)
    private Long transfeeFreeId;

    /**
     * 运费模板id
     */
    @TableField(value = "transport_id")
    private Long transportId;

    /**
     * 包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）
     */
    @TableField(value = "free_type")
    private Integer freeType;

    /**
     * 需满金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 包邮x件/重量/体积
     */
    @TableField(value = "piece")
    private BigDecimal piece;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<Area> freeCityList;
}