package com.bjpowernode.mapper;

import com.bjpowernode.domain.Transfee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjpowernode.domain.Transport;

/**
* @author Administrator
* @description 针对表【transfee】的数据库操作Mapper
* @createDate 2024-03-17 20:34:58
* @Entity com.bjpowernode.domain.Transfee
*/
public interface TransfeeMapper extends BaseMapper<Transfee> {
    public int insert(Transfee transfee);

}




