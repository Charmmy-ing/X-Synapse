package com.charmmy.x_synapse.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private long userId;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
}
