package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.User;
import com.bjpowernode.service.UserService;
import com.bjpowernode.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-03-17 21:12:01
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final UserMapper userMapper;

    @Override
    public boolean setUserInfo(User user) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User selectById = userMapper.selectById(userId);
        selectById.setNickName(user.getNickName());
        selectById.setPic(user.getPic());
        selectById.setSex(user.getSex());
        return userMapper.updateById(selectById) > 0;
    }
}




