package com.bjpowernode.service;

import com.bjpowernode.domain.PickAddr;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【pick_addr(用户配送地址)】的数据库操作Service
* @createDate 2024-03-17 20:34:58
*/
public interface PickAddrService extends IService<PickAddr> {
    public boolean save(PickAddr pickAddr);
}
