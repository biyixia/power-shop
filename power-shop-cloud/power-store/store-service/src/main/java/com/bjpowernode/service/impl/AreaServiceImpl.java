package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Area;
import com.bjpowernode.exception.AppException;
import com.bjpowernode.service.AreaService;
import com.bjpowernode.mapper.AreaMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【area】的数据库操作Service实现
* @createDate 2024-03-17 20:34:58
*/
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area>
    implements AreaService{
    @Override
    public boolean updateById(Area area) {
        if (area.getAreaId().equals(area.getParentId())) {
            throw new AppException("上级地区不能是地区本身");
        }
        return super.updateById(area);
    }

}




