package com.charmmy.x_synapse.service.impl;

import com.charmmy.x_synapse.mapper.PostMapper;
import com.charmmy.x_synapse.pojo.DTO.PostDTO;
import com.charmmy.x_synapse.pojo.PageBean;
import com.charmmy.x_synapse.pojo.Post;
import com.charmmy.x_synapse.service.PostService;
import com.charmmy.x_synapse.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostMapper postMapper;

    @Override
    public void addPost(PostDTO post) {
        postMapper.addPost(post);
    }

    @Override
    public PageBean<PostDTO> getPostPage(Integer pageNum, Integer pageSize) {
        //创建分页对象来存储查询结果
        PageBean<PostDTO> pageBean = new PageBean<>();
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        //调用Mapper查询分页查询
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("userId");
        //调用Mapper
        List<PostDTO> list = postMapper.list(userId);
        Page<PostDTO> page = (Page<PostDTO>) list;
        //设置分页对象的总条数和当前页数据集合
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }
}
