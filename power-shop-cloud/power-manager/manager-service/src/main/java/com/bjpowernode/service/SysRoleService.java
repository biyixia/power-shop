package com.bjpowernode.service;

import com.bjpowernode.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_role(角色)】的数据库操作Service
* @createDate 2024-03-11 00:37:08
*/
public interface SysRoleService extends IService<SysRole> {
    boolean save(SysRole sysRole);

    SysRole getById(Long roleId);

    boolean updateById(SysRole sysRole);

    boolean removeByRoleIds(List<Long> roleIds);

}
