package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.SysRole;
import com.bjpowernode.domain.SysRoleMenu;
import com.bjpowernode.service.SysRoleMenuService;
import com.bjpowernode.service.SysRoleService;
import com.bjpowernode.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【sys_role(角色)】的数据库操作Service实现
 * @createDate 2024-03-11 00:37:08
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuService sysRoleMenuService;

    @Override
    public boolean save(SysRole sysRole) {
        //新增sys_role表数据
        if (sysRoleMapper.insert(sysRole) > 0) {
            //新增sys_role_menu中间数据
            sysRoleMenuService.saveBatch(
                    sysRole.getMenuIdList().stream().map(
                            menuId -> {
                                return SysRoleMenu.builder()
                                        .menuId(menuId)
                                        .roleId(sysRole.getRoleId())
                                        .build();
                            }
                    ).collect(Collectors.toList())
            );
            return true;
        }
        return false;
    }

    @Override
    public SysRole getById(Long roleId) {
        SysRole sysRole = sysRoleMapper.selectById(roleId);
        sysRole.setMenuIdList(
                sysRoleMenuService.list(
                        new LambdaQueryWrapper<SysRoleMenu>()
                                .eq(SysRoleMenu::getRoleId, sysRole.getRoleId())
                ).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList())
        );
        return sysRole;
    }

    @Override
    public boolean updateById(SysRole sysRole) {
        if (sysRoleMapper.updateById(sysRole) > 0) {
            sysRoleMenuService.remove(
                    new LambdaQueryWrapper<SysRoleMenu>()
                            .eq(SysRoleMenu::getRoleId, sysRole.getRoleId())
            );
            sysRoleMenuService.saveBatch(
                    sysRole.getMenuIdList().stream().map(
                            menuId -> {
                                return SysRoleMenu.builder()
                                        .roleId(sysRole.getRoleId())
                                        .menuId(menuId)
                                        .build();
                            }
                    ).collect(Collectors.toList())
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean removeByRoleIds(List<Long> roleIds) {
        if (roleIds.size() > 1) {
            return this.removeByIds(roleIds) &&
                    sysRoleMenuService.removeByIds(
                            sysRoleMenuService.list(
                                    new LambdaQueryWrapper<SysRoleMenu>()
                                            .in(SysRoleMenu::getRoleId, roleIds)
                            ).stream().map(SysRoleMenu::getId).collect(Collectors.toList())
                    );
        } else {
            return sysRoleMapper.deleteById(roleIds.get(0)) > 0 &&
                    sysRoleMenuService.removeByIds(
                            sysRoleMenuService.list(
                                    new LambdaQueryWrapper<SysRoleMenu>()
                                            .eq(SysRoleMenu::getRoleId, roleIds.get(0))
                            ).stream().map(SysRoleMenu::getId).collect(Collectors.toList())
                    );
        }
    }
}




