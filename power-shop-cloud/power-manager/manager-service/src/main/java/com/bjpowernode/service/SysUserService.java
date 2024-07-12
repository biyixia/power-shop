package com.bjpowernode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.domain.SysUser;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户)】的数据库操作Service
* @createDate 2024-03-11 00:37:08
*/
public interface SysUserService extends IService<SysUser> {
    boolean save(SysUser sysUser);

    SysUser getById(Long userId);

    boolean updateById(SysUser sysUser);

    boolean removeById(String userId);
}
