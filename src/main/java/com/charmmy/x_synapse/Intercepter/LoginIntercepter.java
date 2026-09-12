package com.charmmy.x_synapse.Intercepter;

import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.utils.JwtUtil;
import com.charmmy.x_synapse.utils.Md5Util;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
              String token = request.getHeader("Authorization");
              try{
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
