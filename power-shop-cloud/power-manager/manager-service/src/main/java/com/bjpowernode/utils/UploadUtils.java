package com.bjpowernode.utils;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.bjpowernode.properties.QiniuProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;

@Configuration
@RequiredArgsConstructor
public class UploadUtils {
    private final QiniuProperties qiniuProperties;

    public String upload(byte[] bytes) {
        return this.upload(bytes, null);
    }
    public String upload(byte[] bytes, String filename) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        //构造一个带指定 Region 对象的配置类
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = qiniuProperties.getAccessKey();
        String secretKey = qiniuProperties.getSecretKey();
        String bucket = qiniuProperties.getBucketName();
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filename;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(inputStream,key,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return qiniuProperties.getHostAddr()+ "/" + putRet.key;
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);
                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }
}
