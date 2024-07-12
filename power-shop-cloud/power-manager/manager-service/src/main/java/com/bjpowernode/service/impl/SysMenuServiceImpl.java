package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.SysMenu;
import com.bjpowernode.exception.AppException;
import com.bjpowernode.service.SysMenuService;
import com.bjpowernode.mapper.SysMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_menu(菜单管理)】的数据库操作Service实现
* @createDate 2024-03-11 00:37:08
*/
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{
    private final SysMenuMapper sysMenuMapper;
    @Override
    public List<SysMenu> selectMenuList(String userId) {
        return sysMenuMapper.getMenuList(userId);
    }

    @Override
    public boolean removeById(Long menuId){
        if (sysMenuMapper.selectCount(
                new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getParentId, menuId)
        ) > 0) {
            throw new AppException("请先删除该菜单下的子菜单！");
        }
        return sysMenuMapper.deleteById(menuId) > 0;
    }
}




