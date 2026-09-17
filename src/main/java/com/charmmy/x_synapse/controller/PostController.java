package com.charmmy.x_synapse.controller;

import com.charmmy.x_synapse.DTO.PostDTO;
import com.charmmy.x_synapse.pojo.PageBean;
import com.charmmy.x_synapse.pojo.Result;
import com.charmmy.x_synapse.service.PostService;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    //添加帖子
    @PostMapping
    public Result addPost(@RequestBody PostDTO post) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Long id = userId.longValue();
        post.setUserId(id);
        postService.addPost(post);
        return Result.success();
    }
    //分页获取帖子
    @GetMapping("/page")
    public PageBean<PostDTO> getPostPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        PageBean<PostDTO> pageBean = postService.getPostPage(pageNum, pageSize);
        return pageBean;
    }
}
