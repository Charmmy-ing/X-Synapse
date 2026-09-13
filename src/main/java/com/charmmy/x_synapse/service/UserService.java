package com.charmmy.x_synapse.service;

import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.pojo.User;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

import java.util.Map;

public interface UserService {
    User findUserById( String username) ;
    void register(String username, String password);

    void updateUserInfo(User user);

    void updateAvatar(@URL String avatar,String username);

    Result updateUserpassword(Map<String, String> map);
}
