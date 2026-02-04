package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ForumPost;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 论坛帖子Mapper
 */
public interface ForumPostMapper extends BaseMapper<ForumPost> {

    /**
     * 增加浏览次数
     */
    @Update("UPDATE forum_post SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);

    /**
     * 增加点赞数
     */
    @Update("UPDATE forum_post SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id);

    /**
     * 减少点赞数
     */
    @Update("UPDATE forum_post SET like_count = like_count - 1 WHERE id = #{id} AND like_count > 0")
    int decrementLikeCount(@Param("id") Long id);

    /**
     * 增加评论数
     */
    @Update("UPDATE forum_post SET comment_count = comment_count + 1 WHERE id = #{id}")
    int incrementCommentCount(@Param("id") Long id);

    /**
     * 减少评论数
     */
    @Update("UPDATE forum_post SET comment_count = comment_count - 1 WHERE id = #{id} AND comment_count > 0")
    int decrementCommentCount(@Param("id") Long id);
}
