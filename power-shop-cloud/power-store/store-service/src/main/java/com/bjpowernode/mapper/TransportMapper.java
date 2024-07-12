package com.bjpowernode.mapper;

import com.bjpowernode.domain.Transport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【transport】的数据库操作Mapper
* @createDate 2024-03-17 20:34:58
* @Entity com.bjpowernode.domain.Transport
*/
public interface TransportMapper extends BaseMapper<Transport> {
    public int insert(Transport transport);
}




