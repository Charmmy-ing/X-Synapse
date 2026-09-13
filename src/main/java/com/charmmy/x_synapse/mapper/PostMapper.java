package com.charmmy.x_synapse.mapper;

import com.charmmy.x_synapse.pojo.DTO.PostDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
        // 添加帖子
        @Insert("insert into post (user_id, content,state, like_count, comment_count, create_time, update_time) values (#{userId}, #{content}, #{state}, 0, 0, now(), now())")
        void addPost(PostDTO post);

        List<PostDTO> list(Integer userId);
}
