package com.charmmy.x_synapse.utils;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.*;
import com.aliyun.sdk.service.oss2.paginator.ListBucketsIterable;

import java.io.InputStream;

public class AliOSSUtil {
    static String  REGION = "cn-beijing";
    static String ACCESS_KEY_ID = "";
    static String ACCESS_KEY_SECRET = "";
    public String uploadFile(String objectName, InputStream inputStream) {


       // CredentialsProvider provider = new EnvironmentVariableCredentialsProvider();

        OSSClientBuilder clientBuilder = OSSClient.newBuilder()
                .credentialsProvider(new StaticCredentialsProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET))
                .region(REGION);
           String url = "";
        try (OSSClient client = clientBuilder.build()) {
                url = "https://" + objectName + ".oss-cn-beijing.aliyuncs.com";
            ListBucketsIterable paginator = client.listBucketsPaginator(
                    ListBucketsRequest.newBuilder()
                            .build());

            for (ListBucketsResult result : paginator) {
                for (BucketSummary info : result.buckets()) {
                    System.out.printf("bucket: name:%s, region:%s, storageClass:%s\n", info.name(), info.region(), info.storageClass());
                }
            }

            return url;
        } catch (Exception e) {
//            ServiceException se = ServiceException.asCause(e);
//            if (se != null) {
//                System.out.printf("ServiceException: requestId:%s, errorCode:%s\n", se.requestId(), se.errorCode());
//            }
            System.out.printf("error:\n%s", e);
        }
       return null;
    }
}
