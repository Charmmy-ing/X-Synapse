package com.charmmy.x_synapse.controller;

import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.pojo.User;
import com.charmmy.x_synapse.service.UserService;
import com.charmmy.x_synapse.utils.JwtUtil;
import com.charmmy.x_synapse.utils.Md5Util;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

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
    public Result login(@Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String username
            , @Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String password) {
        User user = userService.findUserById(username);
        if (user == null) {
            return Result.error("用户名不存在");
        }
        if (!Md5Util.getMD5String(password).equals(user.getPassword())) {
            //测试打印出查询到的用户名和密码的，查看是否与数据库中的值一致
//            System.out.println(user.getUsername());
//            System.out.println(Md5Util.getMD5String(password));
//            System.out.println(user.getPassword());
            return Result.error("登录失败");
        }
        //3.登录成功，返回jwt token
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        String token = JwtUtil.genToken(map);
        return Result.success(token);
    }

    //获取用户信息
    @GetMapping("/UserInfo")
    public Result getInfo() {
        //从ThreadLocal中获取用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findUserById(username);
        return Result.success(user);
    }

    //更新用户信息
    @PutMapping("/updateUserInfo")
    //将请求体中的json格式转换为User对象
    public Result updateUserInfo(@RequestBody @Validated User user) {
        userService.updateUserInfo(user);
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
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> map) {
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        String newPasswordRepeat = map.get("newPasswordRepeat");
        if (!newPassword.equals(newPasswordRepeat)) {
            return Result.error("两次密码输入不一致");
        }
        //从ThreadLocal中获取用户信息
        Map<String, Object> map1 = ThreadLocalUtil.get();
        String password = (String) map1.get("password");
        if (password.equals(oldPassword)) {
            //更新用户密码
            password = Md5Util.getMD5String(newPassword);
            String username = (String) map1.get("username");
            userService.updateUserpassword(username,password);
        }
        else{
            return Result.error("旧密码错误");
        }
        return Result.success();
    }
}
