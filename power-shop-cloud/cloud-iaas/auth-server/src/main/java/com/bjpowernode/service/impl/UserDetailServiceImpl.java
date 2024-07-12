package com.bjpowernode.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.consts.AuthConstants;
import com.bjpowernode.domain.SysUser;
import com.bjpowernode.domain.User;
import com.bjpowernode.mapper.SysUserMapper;
import com.bjpowernode.properties.WxProperties;
import com.bjpowernode.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final RestTemplate restTemplate;
    private final WxProperties wxProperties;
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //查看请求头的登录方式
        String loginType = request.getHeader(AuthConstants.LOGIN_TYPE_PREFIX);
        if (StringUtils.isBlank(loginType)) {
            return null;
        }
        switch (loginType) {
            case AuthConstants.SYS_USER:
                //根据用户名查询用户信息
                SysUser sysUser = sysUserMapper.selectOne(
                        new LambdaQueryWrapper<SysUser>().eq(
                                StringUtils.isNotBlank(username),
                                SysUser::getUsername, username
                        )
                );
                //若存在供用户则查询相关的权限
                if (!ObjectUtils.isEmpty(sysUser)) {
                    List<String> auths = sysUserMapper.selectAuthByUserId(sysUser.getUserId());
                    if (!CollectionUtils.isEmpty(auths)) {
                        sysUser.setAuthorities(auths);
                    }
                }
                //SpringSecurity会解密数据中的密码，并与登录的密码比较
                return sysUser;
            case AuthConstants.USER:
                String url = String.format(
                        wxProperties.getTokenUrl(),
                        wxProperties.getAppId(),
                        wxProperties.getAppSecret(),
                        username,
                        wxProperties.getGrantType()
                );
                String wxStr = restTemplate.getForObject(
                        url,
                        String.class
                );
                JSONObject jsonObject = JSON.parseObject(wxStr);
                String openid = jsonObject.getString("openid");
                if (StringUtils.isBlank(openid)) {
                    return null;
                }
                User user = userService.getById(openid);
                if (ObjectUtils.isEmpty(user)) {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    String ip = attributes.getRequest().getRemoteAddr();
                    user = User.builder()
                            .userId(openid)
                            .modifyTime(new DateTime())
                            .userRegtime(new DateTime())
                            .userRegip(ip)
                            .userLasttime(new DateTime())
                            .userLastip(ip)
                            .status(1)
                            .build();
                    userService.save(user);
                }
                return user;
            default:
                return null;
        }
    }
}
