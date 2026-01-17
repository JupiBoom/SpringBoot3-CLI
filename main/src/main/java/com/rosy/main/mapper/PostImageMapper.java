package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.PostImage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 帖子图片Mapper接口
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Mapper
public interface PostImageMapper extends BaseMapper<PostImage> {
}