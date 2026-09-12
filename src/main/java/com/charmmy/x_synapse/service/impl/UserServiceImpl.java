package com.charmmy.x_synapse.service.impl;

import com.charmmy.x_synapse.mapper.UserMapper;
import com.charmmy.x_synapse.pojo.User;
import com.charmmy.x_synapse.service.UserService;
import com.charmmy.x_synapse.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void updateUserpassword(String username, String password) {
        userMapper.updatePassword(username,password);
    }
}
