package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.SysUser;
import com.bjpowernode.service.SysUserService;
import com.bjpowernode.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户)】的数据库操作Service实现
* @createDate 2024-02-27 16:44:35
*/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

    private final SysUserMapper sysUserMapper;
}




