package com.bjpowernode.mapper;

import com.bjpowernode.domain.ProdComm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【prod_comm(商品评论)】的数据库操作Mapper
* @createDate 2024-03-17 18:51:41
* @Entity com.bjpowernode.domain.ProdComm
*/
public interface ProdCommMapper extends BaseMapper<ProdComm> {
    public Long niceCommCount(Long prodId);

    public Long commCount();
}




