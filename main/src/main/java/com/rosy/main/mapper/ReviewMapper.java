package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Review;
import com.rosy.main.domain.vo.ReviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    ReviewVO selectReviewDetail(@Param("id") Long id);

    List<ReviewVO> selectByActivityId(@Param("activityId") Long activityId);

    List<ReviewVO> selectByUserId(@Param("userId") Long userId);

    Review selectByActivityAndUser(@Param("activityId") Long activityId, @Param("userId") Long userId);
}
