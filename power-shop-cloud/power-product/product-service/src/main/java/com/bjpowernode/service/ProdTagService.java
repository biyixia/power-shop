package com.bjpowernode.service;

import com.bjpowernode.domain.ProdTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【prod_tag(商品分组表)】的数据库操作Service
* @createDate 2024-03-17 18:51:41
*/
public interface ProdTagService extends IService<ProdTag> {
    public boolean save(ProdTag prodTag);
    public boolean updateById(ProdTag prodTag);
    public boolean delProdTag(Long prodTagId);

}
