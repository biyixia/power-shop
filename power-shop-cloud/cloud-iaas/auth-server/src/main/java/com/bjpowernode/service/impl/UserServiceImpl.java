package com.bjpowernode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.domain.User;
import com.bjpowernode.service.UserService;
import com.bjpowernode.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-03-23 21:09:53
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




