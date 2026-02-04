package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.Registration;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报名Mapper
 */
public interface RegistrationMapper extends BaseMapper<Registration> {

    /**
     * 查询需要发送提醒的报名记录
     * 活动开始前1小时，且未发送过提醒的已通过报名
     */
    @Select("SELECT r.* FROM registration r " +
            "INNER JOIN activity a ON r.activity_id = a.id " +
            "WHERE r.status = 1 AND r.reminder_sent = 0 " +
            "AND a.start_time BETWEEN #{startTime} AND #{endTime}")
    List<Registration> selectNeedReminderList(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户的报名记录
     */
    @Select("SELECT * FROM registration WHERE user_id = #{userId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<Registration> selectByUserId(@Param("userId") Long userId);

    /**
     * 查询活动的报名记录
     */
    @Select("SELECT * FROM registration WHERE activity_id = #{activityId} AND is_deleted = 0 ORDER BY create_time DESC")
    List<Registration> selectByActivityId(@Param("activityId") Long activityId);

    /**
     * 统计活动的已确认报名人数
     */
    @Select("SELECT COUNT(*) FROM registration WHERE activity_id = #{activityId} AND status = 1 AND is_deleted = 0")
    int countConfirmedByActivityId(@Param("activityId") Long activityId);
}
