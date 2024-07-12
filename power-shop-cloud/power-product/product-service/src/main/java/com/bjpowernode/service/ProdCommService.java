package com.bjpowernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.ProdComm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【prod_comm(商品评论)】的数据库操作Service
* @createDate 2024-03-17 18:51:41
*/
public interface ProdCommService extends IService<ProdComm> {
    public Page<ProdComm> prodCommPageByProd(Long prodId, int current, int size, int evaluate);

    public Page<ProdComm> page(Page<ProdComm> page);
}
