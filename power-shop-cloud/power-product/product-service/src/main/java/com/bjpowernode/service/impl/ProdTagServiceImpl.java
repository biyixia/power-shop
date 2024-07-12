package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.ProdTag;
import com.bjpowernode.service.ProdTagService;
import com.bjpowernode.mapper.ProdTagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【prod_tag(商品分组表)】的数据库操作Service实现
* @createDate 2024-03-17 18:51:41
*/
@Service
@RequiredArgsConstructor
public class ProdTagServiceImpl extends ServiceImpl<ProdTagMapper, ProdTag>
    implements ProdTagService{
    private final ProdTagMapper prodTagMapper;
    @Override
    public boolean save(ProdTag prodTag) {
        prodTag.setShopId(1L);
        prodTag.setIsDefault(0);
        prodTag.setProdCount(0L);
        prodTag.setCreateTime(new DateTime());
        prodTag.setUpdateTime(new DateTime());
        return prodTagMapper.insert(prodTag) > 0;
    }

    @Override
    public boolean updateById(ProdTag prodTag) {
        prodTag.setUpdateTime(new DateTime());
        return prodTagMapper.updateById(prodTag) > 0;
    }

    @Override
    public boolean delProdTag(Long prodTagId) {
        ProdTag prodTag = prodTagMapper.selectById(prodTagId);
        prodTag.setStatus(0);
        prodTag.setDeleteTime(new DateTime());
        return prodTagMapper.updateById(prodTag) > 0;
    }
}




