package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ForumComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 论坛评论Mapper
 */
public interface ForumCommentMapper extends BaseMapper<ForumComment> {

    /**
     * 根据帖子ID查询评论列表
     */
    @Select("SELECT * FROM forum_comment WHERE post_id = #{postId} AND status = 1 AND is_deleted = 0 ORDER BY create_time ASC")
    List<ForumComment> selectByPostId(@Param("postId") Long postId);

    /**
     * 根据父评论ID查询子评论
     */
    @Select("SELECT * FROM forum_comment WHERE parent_id = #{parentId} AND status = 1 AND is_deleted = 0 ORDER BY create_time ASC")
    List<ForumComment> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 增加点赞数
     */
    @Update("UPDATE forum_comment SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id);

    /**
     * 减少点赞数
     */
    @Update("UPDATE forum_comment SET like_count = like_count - 1 WHERE id = #{id} AND like_count > 0")
    int decrementLikeCount(@Param("id") Long id);
}
