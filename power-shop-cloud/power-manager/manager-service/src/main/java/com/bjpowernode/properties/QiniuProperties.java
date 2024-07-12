package com.bjpowernode.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "qiniu")
public class QiniuProperties {
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String hostAddr;

}
