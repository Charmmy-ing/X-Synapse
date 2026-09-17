package com.charmmy.x_synapse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charmmy.x_synapse.mapper.UserMapper;
import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.pojo.User;
import com.charmmy.x_synapse.service.UserService;
import com.charmmy.x_synapse.utils.JwtUtil;
import com.charmmy.x_synapse.utils.Md5Util;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate StringRedisTemplate;

    @Override
    public String findUserByUsername(String username,String password)  {
        //1.根据用户名查询用户
        LambdaQueryWrapper<User> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(User::getUsername,username);
        User user=userMapper.selectOne(Wrapper);

            if (user == null) {
               throw new RuntimeException("用户名不存在");
            }
            if (!Md5Util.getMD5String(password).equals(user.getPassword())) {
                //测试打印出查询到的用户名和密码的，查看是否与数据库中的值一致
    //            System.out.println(user.getUsername());
    //            System.out.println(Md5Util.getMD5String(password));
    //            System.out.println(user.getPassword());
              throw new RuntimeException("登录失败");
            }

        //3.登录成功，返回jwt token
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("id", user.getId());
        String token = JwtUtil.genToken(map);
         StringRedisTemplate.opsForValue()
            .set("token", token,1, TimeUnit.HOURS);
        return token;
    }

    @Override
    public User findUserById(String username)  {
        //1.根据用户名查询用户
        LambdaQueryWrapper<User> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(User::getUsername,username);
        User user=userMapper.selectOne(Wrapper);
        if(user==null){
            return null;
        }
        return user;
    }


    @Override
    public void register(String username, String password) {
        User user = new User();
        String Password= Md5Util.getMD5String(password);
        user.setUsername(username);
        user.setPassword(Password);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        //2.添加用户
        userMapper.insert(user);
    }

    @Override
    public void updateUserInfo(User updateUser){
        updateUser.setUpdateTime(LocalDateTime.now());;
        userMapper.updateById(updateUser);
    }

    @Override
    public void updateAvatar(String avatar,String username) {
        User user = new User();
        LambdaQueryWrapper<User> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(User::getUsername,username);
        user.setAvatar(avatar);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user,Wrapper);
    }

    @Override
    public Result updateUserpassword(Map<String, String> map) {
        String oldPassword=map.get("oldPassword");String new_Password=map.get("newPassword");String rePassword=map.get("rePassword");
        Map<String,String> usermap= ThreadLocalUtil.get();
        String username=usermap.get("username");
        LambdaQueryWrapper<User> Wrapper = new LambdaQueryWrapper<>();
        User user=userMapper.selectOne(Wrapper.eq(User::getUsername,username));
        String password=user.getPassword();
        String newPassword= Md5Util.getMD5String(new_Password);
        if(!password.equals(Md5Util.getMD5String(oldPassword))){
            return Result.error("旧密码错误");
        }else if(!new_Password.equals(rePassword)){
            return Result.error("两次输入的密码不一致");
        }else if (password.equals(newPassword)) {
            return Result.error("新密码不能与旧密码相同");
        }
        Wrapper.eq(User::getUsername,username);
        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user,Wrapper);
        StringRedisTemplate.delete("token");
        return Result.success("密码更新成功");
    }
}
