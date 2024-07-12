package com.bjpowernode.service;

import com.bjpowernode.domain.Area;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【area】的数据库操作Service
* @createDate 2024-03-17 20:34:58
*/
public interface AreaService extends IService<Area> {
    boolean updateById(Area area);
}
