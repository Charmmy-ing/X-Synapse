package com.charmmy.x_synapse.service;

import com.charmmy.x_synapse.pojo.User;
import jakarta.validation.constraints.Pattern;

public interface UserService {
    User findUserById( String username) ;
    void register(String username, String password);
}
