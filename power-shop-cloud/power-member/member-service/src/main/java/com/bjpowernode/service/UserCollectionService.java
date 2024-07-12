package com.bjpowernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.Prod;
import com.bjpowernode.domain.UserCollection;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user_collection】的数据库操作Service
* @createDate 2024-03-17 21:12:01
*/
public interface UserCollectionService extends IService<UserCollection> {

    Page<Prod> getProds(Page<Prod> page);

    public boolean addOrCancel(Long prodId);
}
