package com.bjpowernode.mapper;

import com.bjpowernode.domain.Prod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【prod(商品)】的数据库操作Mapper
* @createDate 2024-03-17 18:51:41
* @Entity com.bjpowernode.domain.Prod
*/
public interface ProdMapper extends BaseMapper<Prod> {
    public int insert(Prod prod);
    public Integer count();
}




