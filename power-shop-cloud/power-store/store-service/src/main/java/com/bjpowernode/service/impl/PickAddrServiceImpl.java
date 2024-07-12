package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.PickAddr;
import com.bjpowernode.service.PickAddrService;
import com.bjpowernode.mapper.PickAddrMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【pick_addr(用户配送地址)】的数据库操作Service实现
* @createDate 2024-03-17 20:34:58
*/
@Service
public class PickAddrServiceImpl extends ServiceImpl<PickAddrMapper, PickAddr>
    implements PickAddrService{
    @Override
    public boolean save(PickAddr pickAddr) {
        pickAddr.setShopId(1L);
        return super.save(pickAddr);
    }
}




