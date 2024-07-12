package com.bjpowernode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.domain.User;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-03-17 21:12:01
*/
public interface UserService extends IService<User> {

    public boolean setUserInfo(User user);
}
