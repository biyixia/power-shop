package com.bjpowernode.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    private final RedisConnectionFactory redisConnectionFactory;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("pw")
                .secret(bCryptPasswordEncoder.encode("pw-secret"))
                .accessTokenValiditySeconds(7200)
                .authorizedGrantTypes("password")
                .scopes("all")
                .redirectUris("http://www.bjpowernode.com")
                .and()
                .withClient("web")
                .secret(bCryptPasswordEncoder.encode("web-secret"))
                .accessTokenValiditySeconds(Integer.MAX_VALUE)
                .authorizedGrantTypes("client_credentials")
                .scopes("all")
                .redirectUris("http://www.bjpowernode.com");
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        //默认生成token方式为UUID
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //1. 加载私钥
        ClassPathResource resource = new ClassPathResource("rsa/dbc.jks");
        //2. 创建钥匙工厂对象
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(resource, "123456".toCharArray());
        //3. 获取秘钥对象，参数为别名
        KeyPair keyPair = factory.getKeyPair("dbc");
        //4. 通过秘钥生成token令牌
        jwtAccessTokenConverter.setKeyPair(keyPair);
        //指定非对称加密生成令牌,通过固定的秘钥对数据进行加密
        return jwtAccessTokenConverter;
    }
    @Bean
    public TokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //密码授权时需要使用认证管理器对象
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(jwtTokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }
}
