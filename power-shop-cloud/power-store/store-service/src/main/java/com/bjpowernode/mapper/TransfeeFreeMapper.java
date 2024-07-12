package com.bjpowernode.mapper;

import com.bjpowernode.domain.TransfeeFree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjpowernode.domain.Transport;

/**
* @author Administrator
* @description 针对表【transfee_free】的数据库操作Mapper
* @createDate 2024-03-17 20:34:58
* @Entity com.bjpowernode.domain.TransfeeFree
*/
public interface TransfeeFreeMapper extends BaseMapper<TransfeeFree> {
    public int insert(TransfeeFree transfeeFree);

}




