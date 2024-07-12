package com.bjpowernode.config;

import com.bjpowernode.service.impl.SysUserServiceImpl;
import com.bjpowernode.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailServiceImpl userDetailService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //基于数据库的认证方式
        auth.userDetailsService(userDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //任何请求需要授权
        http.authorizeRequests()
                .anyRequest()
                .authenticated();
    }

    //密码授权时所需要使用的认证管理器对象
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
