package com.charmmy.x_synapse.service;

import com.charmmy.x_synapse.DTO.PostDTO;
import com.charmmy.x_synapse.pojo.PageBean;

public interface PostService {
    void addPost(PostDTO post) ;


    PageBean<PostDTO> getPostPage(Integer pageNum, Integer pageSize);
}
