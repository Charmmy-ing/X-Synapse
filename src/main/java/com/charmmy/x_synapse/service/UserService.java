package com.charmmy.x_synapse.service;

import com.charmmy.x_synapse.pojo.User;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

public interface UserService {
    User findUserById( String username) ;
    void register(String username, String password);

    void updateUserInfo(User user);

    void updateAvatar(@URL String avatar,String username);

    void updateUserpassword(String username, String password);
}
