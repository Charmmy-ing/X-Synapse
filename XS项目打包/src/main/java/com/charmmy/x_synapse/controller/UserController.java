package com.charmmy.x_synapse.controller;

import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.pojo.User;
import com.charmmy.x_synapse.service.UserService;
import com.charmmy.x_synapse.utils.JwtUtil;
import com.charmmy.x_synapse.utils.Md5Util;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.type.NStringTypeHandler;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    //redis操作
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    //校验用户名和密码格式
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String username
            , @Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String password) {
        //1.判断用户名是否重复
        User user = userService.findUserById(username);
        if (user == null) {
            //将密码转换为md5格式，加密存储
             String Password = Md5Util.getMD5String(password);
            //2.注册用户
            userService.register(username, Password);
            return Result.success("注册成功");
        } else {
            return Result.error("用户名已存在");
        }

    }

    //登录
    @PostMapping("/login")
    public void login(@Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String username
            , @Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String password) {
              userService.findUserByUsername(username,password);
    }

    //获取用户信息
    @GetMapping("/userInfo")
    public Result getInfo() {
        //从ThreadLocal中获取用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findUserById(username);
        return Result.success(user);
    }

    //更新用户信息
    @PutMapping("/update")
    //将请求体中的json格式转换为User对象
    public Result updateUserInfo(@RequestBody @Validated User updateUser) {
        userService.updateUserInfo(updateUser);
        return Result.success();
    }
    //更新用户头像
    @PutMapping("/uptdateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatar) {
        //从ThreadLocal中获取用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        userService.updateAvatar(avatar,username);
        return Result.success();
    }
    //更新用户密码
    @PostMapping("/updatePwd")
    public Result updatePassword(@RequestBody Map<String, String> map) {
     Result result= userService.updateUserpassword(map);
        return result;
    }
}
