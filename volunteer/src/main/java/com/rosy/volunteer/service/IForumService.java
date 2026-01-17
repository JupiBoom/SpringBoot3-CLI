package com.rosy.volunteer.service;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.dto.ForumPostCreateDTO;
import com.rosy.volunteer.domain.vo.ForumPostDetailVO;
import com.rosy.volunteer.domain.vo.ForumPostListVO;

import java.util.List;

public interface IForumService {
    Long createPost(ForumPostCreateDTO dto);
    void updatePost(Long id, String content);
    void deletePost(Long id);
    ForumPostDetailVO getPostDetail(Long id);
    void incrementViewCount(Long id);
    void likePost(Long id);
    List<ForumPostListVO> getPostList(Long activityId, Integer status, String keyword);
    Object getPostPage(Long activityId, Integer status, String keyword, PageRequest pageRequest);
    void addComment(Long postId, Long parentId, String content);
    void deleteComment(Long id);
    void likeComment(Long id);
    List<Object> getComments(Long postId, Long parentId);
}
