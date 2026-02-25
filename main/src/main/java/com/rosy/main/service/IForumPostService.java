package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ForumPostDTO;
import com.rosy.main.domain.entity.ForumPost;

public interface IForumPostService extends IService<ForumPost> {
    ForumPost create(ForumPostDTO dto, Long userId);
    Page<ForumPost> pageQuery(Integer page, Integer size, Long activityId);
    void view(Long postId);
    void like(Long postId);
}
