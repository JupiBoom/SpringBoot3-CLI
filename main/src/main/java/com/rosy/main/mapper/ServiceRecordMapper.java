package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.ServiceRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 服务记录Mapper
 */
public interface ServiceRecordMapper extends BaseMapper<ServiceRecord> {

    /**
     * 查询用户的服务记录
     */
    @Select("SELECT * FROM service_record WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY service_date DESC")
    List<ServiceRecord> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询活动的服务记录
     */
    @Select("SELECT * FROM service_record WHERE activity_id = #{activityId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<ServiceRecord> selectByActivityId(@Param("activityId") Long activityId);

    /**
     * 统计用户的累计服务时长
     */
    @Select("SELECT COALESCE(SUM(duration_hours), 0) FROM service_record WHERE user_id = #{userId} AND is_deleted = 0")
    BigDecimal sumDurationHoursByUserId(@Param("userId") Long userId);

    /**
     * 统计用户的累计服务次数
     */
    @Select("SELECT COUNT(*) FROM service_record WHERE user_id = #{userId} AND is_deleted = 0")
    int countByUserId(@Param("userId") Long userId);

    /**
     * 根据报名ID查询服务记录
     */
    @Select("SELECT * FROM service_record WHERE registration_id = #{registrationId} AND is_deleted = 0 LIMIT 1")
    ServiceRecord selectByRegistrationId(@Param("registrationId") Long registrationId);
}
