package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.SysUser;
import com.bjpowernode.domain.SysUserRole;
import com.bjpowernode.service.SysUserRoleService;
import com.bjpowernode.service.SysUserService;
import com.bjpowernode.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户)】的数据库操作Service实现
* @createDate 2024-03-11 00:37:08
*/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleService sysUserRoleService;

    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public boolean save(SysUser sysUser) {
        return sysUserMapper.insert(
                sysUser.setPassword(bCryptPasswordEncoder().encode(sysUser.getPassword()))
                        .setCreateUserId(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()))
                        .setCreateTime(new Date())
                        .setShopId(1L)
        ) > 0 && sysUserRoleService.saveBatch(
                sysUser.getRoleIdList().stream().map(
                        roleId -> {
                            return SysUserRole.builder()
                                    .roleId(roleId)
                                    .userId(sysUser.getUserId())
                                    .build();
                        }
                ).collect(Collectors.toList())
        );
    }

    @Override
    public SysUser getById(Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        sysUser.setRoleIdList(
                sysUserRoleService.list(
                        new LambdaQueryWrapper<SysUserRole>()
                                .eq(SysUserRole::getUserId, userId)
                ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList())
        );
        return sysUser;
    }

    @Override
    public boolean updateById(SysUser sysUser) {
        sysUser.setPassword(bCryptPasswordEncoder().encode(sysUser.getPassword()));
        if (sysUserMapper.updateById(sysUser) > 0) {
            sysUserRoleService.remove(
                    new LambdaQueryWrapper<SysUserRole>()
                            .eq(SysUserRole::getUserId, sysUser.getUserId())
            );
            sysUserRoleService.saveBatch(
                    sysUser.getRoleIdList().stream().map(
                            roleId -> {
                                return SysUserRole.builder()
                                        .roleId(roleId)
                                        .userId(sysUser.getUserId())
                                        .build();
                            }
                    ).collect(Collectors.toList())
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean removeById(String userId) {
        return sysUserMapper.deleteById(userId) > 0 &&
                sysUserRoleService.removeByIds(
                        sysUserRoleService.list(
                                new LambdaQueryWrapper<SysUserRole>()
                                        .eq(SysUserRole::getUserId, userId)
                        ).stream().map(SysUserRole::getId).collect(Collectors.toList())
                );
    }
}




