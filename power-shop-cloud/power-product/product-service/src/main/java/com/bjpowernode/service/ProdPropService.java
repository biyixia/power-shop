package com.bjpowernode.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.domain.ProdProp;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
* @author Administrator
* @description 针对表【prod_prop】的数据库操作Service
* @createDate 2024-03-17 18:51:41
*/
public interface ProdPropService extends IService<ProdProp> {
    public Page<ProdProp> page(Page<ProdProp> page);
    public boolean save(ProdProp prodProp);
    public boolean updateById(ProdProp prodProp);
    public boolean removeById(@PathVariable Long prodPropId);
}
