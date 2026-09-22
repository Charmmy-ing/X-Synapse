package com.charmmy.x_synapse.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NonNull
    private Long id;
    @NotEmpty
    private String username;
    @JsonIgnore
    private String password;
    // 头像地址
    private String avatar;
    // 个人简介
    private String bio;
    //注册时间
    private LocalDateTime createTime;
    //信息最后更新时间
    private LocalDateTime updateTime;
}
