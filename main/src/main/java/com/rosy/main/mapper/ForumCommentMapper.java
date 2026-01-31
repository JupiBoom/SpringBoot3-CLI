package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ForumComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 论坛评论Mapper接口
 */
@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {

}