package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ForumReply;
import com.rosy.main.domain.vo.ForumReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForumReplyMapper extends BaseMapper<ForumReply> {

    List<ForumReplyVO> selectByPostId(@Param("postId") Long postId);
}
