package com.charmmy.x_synapse.controller;

import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.utils.AliOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class PicUploadController {
    @Autowired
    private AliOSSUtil ossUtil;
    /**
     * @param objectName 用户上传文件名
     * @param inputStream 文件流
     * @return 文件URL给前端使用
     * 上传文件到OSS
     */
    public Result upload(String objectName, InputStream inputStream) {
        //先调用UUID生成不重复的文件名，再上传文件到OSS (因为重复的文件名会导致覆盖旧文件)
        String fileName = UUID.randomUUID() +objectName.substring(objectName.lastIndexOf("."));
        String url = ossUtil.uploadFile(fileName, inputStream);
        return Result.success(url);
    }

}
