package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Activity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 活动Mapper
 */
public interface ActivityMapper extends BaseMapper<Activity> {

    /**
     * 增加浏览次数
     */
    @Update("UPDATE activity SET view_count = view_count + 1 WHERE id = #{id}")
    int incrementViewCount(@Param("id") Long id);

    /**
     * 增加已报名人数
     */
    @Update("UPDATE activity SET registered_people = registered_people + 1 WHERE id = #{id}")
    int incrementRegisteredPeople(@Param("id") Long id);

    /**
     * 减少已报名人数
     */
    @Update("UPDATE activity SET registered_people = registered_people - 1 WHERE id = #{id} AND registered_people > 0")
    int decrementRegisteredPeople(@Param("id") Long id);

    /**
     * 增加已确认人数
     */
    @Update("UPDATE activity SET confirmed_people = confirmed_people + 1 WHERE id = #{id}")
    int incrementConfirmedPeople(@Param("id") Long id);

    /**
     * 减少已确认人数
     */
    @Update("UPDATE activity SET confirmed_people = confirmed_people - 1 WHERE id = #{id} AND confirmed_people > 0")
    int decrementConfirmedPeople(@Param("id") Long id);
}
