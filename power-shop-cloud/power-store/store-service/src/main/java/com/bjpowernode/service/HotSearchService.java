package com.bjpowernode.service;

import com.bjpowernode.domain.HotSearch;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【hot_search(热搜)】的数据库操作Service
* @createDate 2024-03-17 20:34:58
*/
public interface HotSearchService extends IService<HotSearch> {
    boolean save(HotSearch hotSearch);
    boolean updateById(HotSearch hotSearch);
}
