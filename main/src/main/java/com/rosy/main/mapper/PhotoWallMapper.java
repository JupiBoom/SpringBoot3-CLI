package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.PhotoWall;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 照片墙Mapper
 */
public interface PhotoWallMapper extends BaseMapper<PhotoWall> {

    /**
     * 查询精选照片
     */
    @Select("SELECT * FROM photo_wall WHERE is_featured = 1 AND status = 1 AND is_deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<PhotoWall> selectFeaturedPhotos(@Param("limit") int limit);

    /**
     * 增加点赞数
     */
    @Update("UPDATE photo_wall SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id);

    /**
     * 减少点赞数
     */
    @Update("UPDATE photo_wall SET like_count = like_count - 1 WHERE id = #{id} AND like_count > 0")
    int decrementLikeCount(@Param("id") Long id);
}
