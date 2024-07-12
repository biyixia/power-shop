package com.bjpowernode.service;

import com.bjpowernode.domain.UserAddr;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user_addr(用户配送地址)】的数据库操作Service
* @createDate 2024-03-17 21:12:01
*/
public interface UserAddrService extends IService<UserAddr> {
    public boolean save(UserAddr userAddr);

    public boolean setDefaultAddr(String addrId);
    public boolean updateById(UserAddr userAddr);
}
