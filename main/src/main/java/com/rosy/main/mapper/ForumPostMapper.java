package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛帖子Mapper接口
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {
}