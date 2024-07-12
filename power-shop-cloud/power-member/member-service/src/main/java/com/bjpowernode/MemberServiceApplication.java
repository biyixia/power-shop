package com.bjpowernode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableFeignClients
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }
}
