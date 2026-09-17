package com.charmmy.x_synapse.utils;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.*;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import java.io.InputStream;
import org.springframework.stereotype.Component;

@Component
// 阿里云OSS工具类
public class AliOSSUtil {
    static String  REGION = "cn-beijing";
    static String BUCKET_NAME = "Big-Event-cnlu";
    static String ACCESS_KEY_ID = "";
    static String ACCESS_KEY_SECRET = "";
    public String uploadFile(String objectName, InputStream inputStream) {


       // CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();

        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(new StaticCredentialsProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET))
                .region(REGION);
           String url = "";
        try (OSSClient client = clientBuilder.build()) {
                url = "https://" + BUCKET_NAME + ".oss-cn-beijing.aliyuncs.com/"+objectName;
            PutObjectRequest request = PutObjectRequest.newBuilder()
                    .bucket(BUCKET_NAME)
                    .key(objectName)
                    .body(BinaryData.fromStream(inputStream, null))
                    .build();
            client.putObject(request);
            System.out.println("上传成功");
        } catch (Exception e) {
            System.out.printf("error:\n%s", e);
        }
        return url;
    }
}
