package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.Notice;
import com.bjpowernode.service.NoticeService;
import com.bjpowernode.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【notice】的数据库操作Service实现
* @createDate 2024-03-17 20:34:58
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
    implements NoticeService{
    @Override
    public boolean save(Notice notice) {
        notice.setShopId(1L);
        notice.setPublishTime(new DateTime());
        return super.save(notice);
    }

    @Override
    public boolean updateById(Notice notice) {
        notice.setUpdateTime(new DateTime());
        return super.updateById(notice);
    }
}




