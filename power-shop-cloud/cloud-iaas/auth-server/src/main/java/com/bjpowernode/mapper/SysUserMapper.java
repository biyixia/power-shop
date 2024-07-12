package com.bjpowernode.mapper;

import com.bjpowernode.domain.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户)】的数据库操作Mapper
* @createDate 2024-02-27 17:15:34
* @Entity com.bjpowernode.domain.SysUser
*/
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT sm.perms\n" +
            "FROM sys_user AS su join sys_user_role AS sur ON su.user_id = sur.user_id\n" +
            "    join sys_role_menu AS srm ON srm.role_id = sur.role_id\n" +
            "    JOIN sys_menu AS sm ON sm.menu_id = srm.menu_id\n" +
            "WHERE sm.type > 1 AND su.user_id = #{value}")
    List<String> selectAuthByUserId(Long userId);
}




