package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.vo.ForumPostVO;
import com.rosy.main.dto.req.ForumPostAddRequest;
import com.rosy.main.dto.req.ForumPostUpdateRequest;
import com.rosy.main.dto.req.ForumPostQueryRequest;
import com.rosy.main.dto.req.PostImageAddRequest;

import java.util.List;

public interface IForumPostService extends IService<ForumPost> {

    /**
     * 添加论坛帖子
     */
    Long addForumPost(ForumPostAddRequest request);

    /**
     * 更新论坛帖子
     */
    void updateForumPost(ForumPostUpdateRequest request);

    /**
     * 删除论坛帖子
     */
    void deleteForumPost(Long id);

    /**
     * 根据ID获取帖子VO
     */
    ForumPostVO getForumPostVO(Long id);

    /**
     * 分页查询帖子
     */
    List<ForumPostVO> listForumPostVO(ForumPostQueryRequest request);

    /**
     * 增加浏览量
     */
    void incrementViewCount(Long id);

    /**
     * 点赞/取消点赞
     */
    void toggleLike(Long id);

    /**
     * 上传帖子图片
     */
    void uploadPostImage(Long postId, PostImageAddRequest request);

    /**
     * 删除帖子图片
     */
    void deletePostImage(Long imageId);

    /**
     * 获取活动照片墙
     */
    List<String> getActivityPhotoWall(Long activityId);

    /**
     * 获取热门帖子
     */
    List<ForumPostVO> getHotPosts(int limit);

    /**
     * 获取志愿者帖子
     */
    List<ForumPostVO> getVolunteerPosts(Long userId);
}