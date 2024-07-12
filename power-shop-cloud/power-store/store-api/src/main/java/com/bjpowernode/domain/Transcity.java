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
 * @TableName transcity
 */
@TableName(value ="transcity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class Transcity implements Serializable {
    /**
     * 
     */
    @TableId(value = "transcity_id", type = IdType.AUTO)
    private Long transcityId;

    /**
     * 运费项id
     */
    @TableField(value = "transfee_id")
    private Long transfeeId;

    /**
     * 城市id
     */
    @TableField(value = "city_id")
    private Long cityId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}