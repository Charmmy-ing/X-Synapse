package com.charmmy.x_synapse.Intercepter;

import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.utils.JwtUtil;
import com.charmmy.x_synapse.utils.Md5Util;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

//登录权限的拦截器
@Component
public class LoginIntercepter implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
              String token = request.getHeader("Authorization");
              String redisToken = stringRedisTemplate.opsForValue().get(token);
              try{
                  if(redisToken==null){
                     throw new RuntimeException();
                  }
                   Map<String, Object> map = JwtUtil.parseToken(token);
                   ThreadLocalUtil.set(map);
                   return true;

              }catch(Exception e){
                     response.setStatus(401);
                     return false;
              }
    }
    //关闭ThreadLocalUtil防止内存泄漏
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
