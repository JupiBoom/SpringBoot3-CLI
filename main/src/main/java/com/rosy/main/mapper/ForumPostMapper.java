package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ForumPost;
import com.rosy.main.domain.vo.ForumPostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForumPostMapper extends BaseMapper<ForumPost> {

    ForumPostVO selectForumPostDetail(@Param("id") Long id);

    List<ForumPostVO> selectByActivityId(@Param("activityId") Long activityId);

    List<ForumPostVO> selectByUserId(@Param("userId") Long userId);
}
