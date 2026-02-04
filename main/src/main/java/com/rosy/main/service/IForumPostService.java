package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ForumPostCreateRequest;
import com.rosy.main.domain.dto.ForumPostQueryRequest;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.vo.ForumPostVO;

import java.util.List;

/**
 * 论坛帖子服务接口
 */
public interface IForumPostService extends IService<ForumPost> {

    /**
     * 创建帖子
     */
    Long createPost(ForumPostCreateRequest request, Long userId);

    /**
     * 删除帖子
     */
    boolean deletePost(Long id, Long userId);

    /**
     * 根据ID获取帖子
     */
    ForumPostVO getPostById(Long id);

    /**
     * 分页查询帖子列表
     */
    Page<ForumPostVO> listPosts(ForumPostQueryRequest request);

    /**
     * 获取置顶帖子
     */
    List<ForumPostVO> getTopPosts();

    /**
     * 获取精华帖子
     */
    List<ForumPostVO> getEssencePosts();

    /**
     * 设置置顶
     */
    boolean setTop(Long id, Integer isTop);

    /**
     * 设置精华
     */
    boolean setEssence(Long id, Integer isEssence);

    /**
     * 审核帖子
     */
    boolean reviewPost(Long id, Integer status);

    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);

    /**
     * 获取用户的帖子列表
     */
    List<ForumPostVO> getUserPosts(Long userId);
}
