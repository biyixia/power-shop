package com.bjpowernode.utils;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.bjpowernode.Properties.AliyunProperties;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Configuration
@RequiredArgsConstructor
public class SmsUtils {
    private final AliyunProperties aliyunProperties;
    public boolean sendSms(String phoneNumbers, String code) throws ExecutionException, InterruptedException {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyunProperties.getAccessKeyId())
                .accessKeySecret(aliyunProperties.getAccessKeySecret())
                .build());
        AsyncClient client = AsyncClient.builder()
                .region("cn-shenzhen")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(aliyunProperties.getSignName())
                .templateCode(aliyunProperties.getTemplateCode())
                .phoneNumbers(phoneNumbers)
                .templateParam("{\"code\":"+ code +"}")
                .build();
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));
        client.close();
        return true;
    }
}
