package com.bjpowernode.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@ConfigurationProperties(prefix = "wx.login")
public class WxProperties {
    private String appId;
    private String appSecret;
    private String grantType;
    private String tokenUrl;
    private String urlType;
}
