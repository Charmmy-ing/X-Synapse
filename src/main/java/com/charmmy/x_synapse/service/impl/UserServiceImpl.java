package com.charmmy.x_synapse.service.impl;

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
    public String findUserByUsername(String username,String password) {
        User user=userMapper.selectByUsername(username);
        try {
            if (user == null) {
               throw new Exception("用户名不存在");
            }
            if (!Md5Util.getMD5String(password).equals(user.getPassword())) {
                //测试打印出查询到的用户名和密码的，查看是否与数据库中的值一致
    //            System.out.println(user.getUsername());
    //            System.out.println(Md5Util.getMD5String(password));
    //            System.out.println(user.getPassword());
              throw new Exception("登录失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //3.登录成功，返回jwt token
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("id", user.getId());
        String token = JwtUtil.genToken(map);
        ValueOperations<String, String> valueOperations = StringRedisTemplate.opsForValue();
        valueOperations.set("token", token,1, TimeUnit.HOURS);
        return token;
    }

    @Override
    public User findUserById(String username)  {
        User user=userMapper.selectById(username);
        if(user==null){
            return null;
        }
        return user;
    }


    @Override
    public void register(String username, String password) {
        String Password= Md5Util.getMD5String(password);
        userMapper.addUser(username,Password);
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatar,String username) {
        userMapper.updateAvatar(avatar,username);
    }

    @Override
    public Result updateUserpassword(Map<String, String> map) {
        String oldPassword=map.get("oldPassword");
        String new_Password=map.get("newPassword");
        String rePassword=map.get("rePassword");
        Map<String,String> usermap= ThreadLocalUtil.get();
        String username=usermap.get("username");
        User user=userMapper.selectByUsername(username);
        String password=user.getPassword();
        String newPassword= Md5Util.getMD5String(new_Password);
        if(!password.equals(Md5Util.getMD5String(oldPassword))){
            return Result.error("旧密码错误");
        }else if(!new_Password.equals(rePassword)){
            return Result.error("两次输入的密码不一致");
        }else if (password.equals(newPassword)) {
            return Result.error("新密码不能与旧密码相同");
        }
        userMapper.updatePassword(username,newPassword);
        StringRedisTemplate.delete("token");
        return Result.success("密码更新成功");
    }
}
