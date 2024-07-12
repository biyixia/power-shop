package com.bjpowernode.service;

import com.bjpowernode.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aspectj.weaver.ast.Not;

/**
* @author Administrator
* @description 针对表【notice】的数据库操作Service
* @createDate 2024-03-17 20:34:58
*/
public interface NoticeService extends IService<Notice> {
    public boolean save(Notice notice);
    public boolean updateById(Notice notice);
}
