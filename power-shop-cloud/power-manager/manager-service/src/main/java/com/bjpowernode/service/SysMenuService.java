package com.bjpowernode.service;

import com.bjpowernode.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.exception.AppException;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单管理)】的数据库操作Service
* @createDate 2024-03-11 00:37:08
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> selectMenuList(String userId);

    boolean removeById(Long menuId);

}
