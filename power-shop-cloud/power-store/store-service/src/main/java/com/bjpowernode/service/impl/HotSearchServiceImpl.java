package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.HotSearch;
import com.bjpowernode.service.HotSearchService;
import com.bjpowernode.mapper.HotSearchMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【hot_search(热搜)】的数据库操作Service实现
* @createDate 2024-03-17 20:34:58
*/
@Service
public class HotSearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch>
    implements HotSearchService{
    @Override
    public boolean save(HotSearch hotSearch) {
        hotSearch.setShopId(1L);
        hotSearch.setRecDate(new DateTime());
        return super.save(hotSearch);
    }

    @Override
    public boolean updateById(HotSearch hotSearch) {
        hotSearch.setRecDate(new DateTime());
        return super.updateById(hotSearch);
    }

}




