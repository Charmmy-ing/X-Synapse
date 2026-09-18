package com.charmmy.x_synapse.Intercepter;

import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

//刷新token的拦截器
@Component
public class RefrashIntercepter implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先获取请求头中的token
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken == null) {
            return true;
        }

        // 当为登录状态时从redis中获取token
       Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("token");
        //把map里的值放到threadlocal里
        ThreadLocalUtil.set(map);
        //刷新token
        stringRedisTemplate.expire("token", 30, TimeUnit.MINUTES);
        return true;
    }
}
