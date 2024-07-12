package com.bjpowernode.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.bjpowernode.domain.Transfee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.domain.TransfeeFree;

import java.util.Collection;
import java.util.List;

/**
* @author Administrator
* @description 针对表【transfee】的数据库操作Service
* @createDate 2024-03-17 20:34:58
*/
public interface TransfeeService extends IService<Transfee> {
    public boolean saveBatch(Collection<Transfee> transfees);
    public List<Transfee> list(Wrapper<Transfee> queryWrapper);
    public boolean remove(Wrapper<Transfee> queryWrapper);
}
