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
 * @TableName area
 */
@TableName(value ="area")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class Area implements Serializable {
    /**
     * 
     */
    @TableId(value = "area_id", type = IdType.AUTO)
    private Long areaId;

    /**
     * 
     */
    @TableField(value = "area_name")
    private String areaName;

    /**
     * 
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 
     */
    @TableField(value = "level")
    private Integer level;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}