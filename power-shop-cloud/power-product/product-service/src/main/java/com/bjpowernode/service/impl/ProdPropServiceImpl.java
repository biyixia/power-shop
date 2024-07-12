package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.ProdProp;
import com.bjpowernode.domain.ProdPropValue;
import com.bjpowernode.mapper.ProdPropValueMapper;
import com.bjpowernode.service.ProdPropService;
import com.bjpowernode.mapper.ProdPropMapper;
import com.bjpowernode.service.ProdPropValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【prod_prop】的数据库操作Service实现
 * @createDate 2024-03-17 18:51:41
 */
@Service
@RequiredArgsConstructor
public class ProdPropServiceImpl extends ServiceImpl<ProdPropMapper, ProdProp>
        implements ProdPropService {
    private final ProdPropMapper prodPropMapper;
    private final ProdPropValueService prodPropValueService;

    @Override
    public Page<ProdProp> page(Page<ProdProp> page) {
        Page<ProdProp> prodPropPage = super.page(page);
        for (ProdProp prodProp : prodPropPage.getRecords()) {
            prodProp.setProdPropValues(
                    prodPropValueService.list(
                            new LambdaQueryWrapper<ProdPropValue>()
                                    .eq(ProdPropValue::getPropId, prodProp.getPropId())
                    )
            );
        }
        return prodPropPage;
    }

    @Override
    public boolean save(ProdProp prodProp) {
        prodProp.setRule(1);
        prodProp.setShopId(1L);
        if (prodPropMapper.insert(prodProp) > 0) {
            for (ProdPropValue prodPropValue : prodProp.getProdPropValues()) {
                prodPropValue.setPropId(prodProp.getPropId());
            }
            return prodPropValueService.saveBatch(prodProp.getProdPropValues());
        }
        return false;
    }

    @Override
    public boolean updateById(ProdProp prodProp) {
        if (prodPropMapper.updateById(prodProp) > 0) {
            return prodPropValueService.remove(
                    new LambdaQueryWrapper<ProdPropValue>()
                            .eq(ProdPropValue::getPropId, prodProp.getPropId())
            ) &&
                    prodPropValueService.saveBatch(prodProp.getProdPropValues());
        }
        return false;
    }

    @Override
    public boolean removeById(Long prodPropId) {
        return prodPropMapper.deleteById(prodPropId) > 0 &&
                 prodPropValueService.remove(
                new LambdaQueryWrapper<ProdPropValue>()
                        .eq(ProdPropValue::getPropId, prodPropId)
        );
    }
}




