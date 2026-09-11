package com.charmmy.x_synapse.controller;

import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.pojo.User;
import com.charmmy.x_synapse.service.UserService;
import com.charmmy.x_synapse.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    //校验用户名和密码格式
    public Result register(@Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String username, @Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String password) {
        //1.判断用户名是否重复
        User user = userService.findUserById(username);
        if (user == null) {
            //2.注册用户
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已存在");
        }

    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String username, @Pattern(regexp = "^[a-zA-Z0-9]{5,16}$") String password) {
        User user = userService.findUserById(username);
        if (user == null) {
            return Result.error("用户名不存在");
        }
        if (!Md5Util.getMD5String(password).equals(user.getPassword())) {
            //测试打印出查询到的用户名和密码的，查看是否与数据库中的值一致
            System.out.println(user.getUsername());
            System.out.println(Md5Util.getMD5String(password));
            System.out.println(user.getPassword());
            return Result.error("登录失败");
        }
        return Result.success("返回jwt token");
    }
}
