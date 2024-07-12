package com.bjpowernode.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName transcity_free
 */
@TableName(value ="transcity_free")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class TranscityFree implements Serializable {
    /**
     * 指定条件包邮城市项id
     */
    @TableId(value = "transcity_free_id", type = IdType.AUTO)
    private Long transcityFreeId;

    /**
     * 指定条件包邮项id
     */
    @TableField(value = "transfee_free_id")
    private Long transfeeFreeId;

    /**
     * 城市id
     */
    @TableField(value = "free_city_id")
    private Long freeCityId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}