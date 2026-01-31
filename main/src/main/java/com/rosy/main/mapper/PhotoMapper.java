package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Photo;
import com.rosy.main.domain.vo.PhotoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {

    List<PhotoVO> selectByActivityId(@Param("activityId") Long activityId);

    List<PhotoVO> selectByUserId(@Param("userId") Long userId);
}
