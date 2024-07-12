package com.bjpowernode.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.bjpowernode.domain.Transfee;
import com.bjpowernode.domain.TransfeeFree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
* @author Administrator
* @description 针对表【transfee_free】的数据库操作Service
* @createDate 2024-03-17 20:34:58
*/
public interface TransfeeFreeService extends IService<TransfeeFree> {
    public boolean saveBatch(Collection<TransfeeFree> transfeeFrees);
    public List<TransfeeFree> list(Wrapper<TransfeeFree> queryWrapper);
    public boolean remove(Wrapper<TransfeeFree> queryWrapper);

}
