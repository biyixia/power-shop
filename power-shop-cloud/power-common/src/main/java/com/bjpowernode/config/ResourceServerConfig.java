package com.bjpowernode.config;

import cn.hutool.core.io.FileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.nio.charset.Charset;

/**
 * 资源服务器
 * 校验token的合法性
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Bean
    public TokenStore resourceTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        String publicKey = FileUtil.readString("rsa/dbc.txt", Charset.defaultCharset());
        jwtAccessTokenConverter.setVerifierKey(publicKey);
        return jwtAccessTokenConverter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(resourceTokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.sessionManagement().disable();
        http.authorizeRequests()
                .antMatchers("/v2/api-docs",
                        // swagger  druid ...
                        "/v3/api-docs",
                        "/swagger-resources/configuration/ui",
                        //用来获取支持的动作
                        "/swagger-resources",
                        //用来获取api-docs的URI
                        "/swagger-resources/configuration/security",
                        //安全选项
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/druid/**",
                        "/actuator/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }
}
