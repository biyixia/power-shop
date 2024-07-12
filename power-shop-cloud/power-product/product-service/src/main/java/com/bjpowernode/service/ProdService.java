package com.bjpowernode.service;

import com.bjpowernode.domain.Prod;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.domain.ProdSkuCount;

import java.util.List;

/**
* @author Administrator
* @description 针对表【prod(商品)】的数据库操作Service
* @createDate 2024-03-17 18:51:41
*/
public interface ProdService extends IService<Prod> {
    public Prod getById(Long prodId);

    public boolean deductMySQLStock(List<ProdSkuCount> prodSkuCounts);

    public boolean save(Prod prod);
    public boolean updateById(Prod prod);
    public boolean removeById(Long prodId);

}
