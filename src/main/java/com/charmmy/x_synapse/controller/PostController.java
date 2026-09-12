package com.charmmy.x_synapse.controller;

import com.charmmy.x_synapse.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {
    @GetMapping("/context")
    public Result context() {
        //会先通过拦截器验证JWT令牌，如果验证失败，会返回401错误码
        //如果验证成功，会继续执行后续的请求处理
        return Result.success("post context");
    }
}
