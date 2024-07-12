package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.UserAddr;
import com.bjpowernode.service.UserAddrService;
import com.bjpowernode.mapper.UserAddrMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【user_addr(用户配送地址)】的数据库操作Service实现
 * @createDate 2024-03-17 21:12:01
 */
@Service
public class UserAddrServiceImpl extends ServiceImpl<UserAddrMapper, UserAddr>
        implements UserAddrService {
    @Override
    public boolean save(UserAddr userAddr) {
        userAddr.setUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        userAddr.setStatus(1);
        userAddr.setCreateTime(new DateTime());
        userAddr.setUpdateTime(new DateTime());
        return super.save(userAddr);
    }

    @Override
    @Transactional
    public boolean setDefaultAddr(String addrId) {
        if (this.count(new LambdaQueryWrapper<UserAddr>()
                .eq(UserAddr::getCommonAddr, 1)
                .eq(UserAddr::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
        ) >= 1) {
            this.updateBatchById(
                    this.list(
                            new LambdaQueryWrapper<UserAddr>()
                                    .eq(UserAddr::getCommonAddr, 1)
                                    .eq(UserAddr::getUserId, SecurityContextHolder.getContext().getAuthentication().getName())
                    ).stream().map(userAddr -> {
                        return userAddr.setCommonAddr(0);
                    }).collect(Collectors.toList())
            );
        }
        return super.updateById(
                this.getById(addrId).setCommonAddr(1).setUpdateTime(new DateTime())
        );
    }

    @Override
    @Transactional

    public boolean updateById(UserAddr userAddr) {
        return super.updateById(this.getById(userAddr.getAddrId()).setUpdateTime(new DateTime()));
    }
}




