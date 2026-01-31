package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ForumPostDTO;
import com.rosy.main.domain.dto.ForumReplyDTO;
import com.rosy.main.domain.dto.PhotoDTO;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.entity.ForumReply;
import com.rosy.main.domain.entity.Photo;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.domain.vo.ForumReplyVO;
import com.rosy.main.domain.vo.PhotoVO;

public interface IForumService extends IService<ForumPost> {

    Long createPost(ForumPostDTO dto, Long userId);

    Long updatePost(Long postId, ForumPostDTO dto, Long userId);

    void deletePost(Long postId, Long userId);

    ForumPostVO getPostDetail(Long postId);

    Page<ForumPostVO> getPostPage(Integer pageNum, Integer pageSize, Long activityId, Long userId);

    Long createReply(ForumReplyDTO dto, Long userId);

    void deleteReply(Long replyId, Long userId);

    Page<ForumReplyVO> getRepliesByPost(Long postId, Integer pageNum, Integer pageSize);

    Long uploadPhoto(PhotoDTO dto, Long userId);

    void deletePhoto(Long photoId, Long userId);

    Page<PhotoVO> getPhotosByActivity(Long activityId, Integer pageNum, Integer pageSize);

    Page<PhotoVO> getPhotoWall(Integer pageNum, Integer pageSize);
}
