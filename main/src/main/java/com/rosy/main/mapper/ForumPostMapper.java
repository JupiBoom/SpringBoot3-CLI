package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {
}
