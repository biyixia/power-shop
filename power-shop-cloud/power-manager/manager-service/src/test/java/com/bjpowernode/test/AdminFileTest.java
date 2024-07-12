package com.bjpowernode.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@SpringBootTest
public class AdminFileTest {

    @Test
    public void fileUpload(MultipartFile file) throws IOException {
        if(file.isEmpty()){
            System.out.println("文件为空");
            return;
        }
        InputStream inputStream = file.getInputStream();
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "wfS15stdsYrgZDLKognMu2VLBUMbzxBiitE_x2ON";
        String secretKey = "bNAET82L_Gm3OOu_5MWYziX4cijMNnxXk8NgI8UJ";
        String bucket = "dsfasefs";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = file.getOriginalFilename();
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(inputStream,key,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
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
    }
}
