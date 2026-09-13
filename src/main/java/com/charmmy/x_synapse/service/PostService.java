package com.charmmy.x_synapse.service;

import com.charmmy.x_synapse.pojo.DTO.PostDTO;
import com.charmmy.x_synapse.pojo.PageBean;
import com.charmmy.x_synapse.pojo.Post;

public interface PostService {
    void addPost(PostDTO post) ;


    PageBean<PostDTO> getPostPage(Integer pageNum, Integer pageSize);
}
