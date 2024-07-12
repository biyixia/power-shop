package com.bjpowernode.mapper;

import com.bjpowernode.domain.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单管理)】的数据库操作Mapper
* @createDate 2024-03-11 00:37:08
* @Entity com.bjpowernode.domain.SysMenu
*/
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getMenuList(String userId);
}




