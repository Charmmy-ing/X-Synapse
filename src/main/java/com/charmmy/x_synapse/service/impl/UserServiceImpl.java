package com.charmmy.x_synapse.service.impl;

import com.charmmy.x_synapse.mapper.UserMapper;
import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.pojo.User;
import com.charmmy.x_synapse.service.UserService;
import com.charmmy.x_synapse.utils.Md5Util;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(String username) {
        User user=userMapper.selectByUsername(username);
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
        return Result.success("密码更新成功");
    }
}
