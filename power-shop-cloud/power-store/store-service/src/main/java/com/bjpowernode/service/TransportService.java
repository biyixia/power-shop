package com.bjpowernode.service;

import com.bjpowernode.domain.Transport;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【transport】的数据库操作Service
* @createDate 2024-03-17 20:34:58
*/
public interface TransportService extends IService<Transport> {
    boolean save(Transport transport);
    Transport getById(Long transportId);
    boolean updateById(Transport transport);
    boolean removeByIds(List<Long> transportIds);

}
