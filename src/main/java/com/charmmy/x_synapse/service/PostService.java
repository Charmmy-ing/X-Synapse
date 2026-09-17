package com.charmmy.x_synapse.service;

import com.charmmy.x_synapse.DTO.PostDTO;
import com.charmmy.x_synapse.pojo.PageBean;

import java.util.List;

public interface PostService {
    void addPost(PostDTO post) ;
    //分页获取帖子
    //PageBean<PostDTO> getPostPage(Integer pageNum, Integer pageSize);

    public List<PostDTO> getPostByUserId(Integer pageNum, Integer pageSize);
}
