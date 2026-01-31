package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

    @Select("SELECT COUNT(*) FROM repair_order WHERE status = #{status} AND is_deleted = 0")
    Long countByStatus(@Param("status") Byte status);

    @Select("SELECT AVG(response_minutes) FROM repair_order WHERE status = 4 AND response_minutes IS NOT NULL AND is_deleted = 0")
    Double getAvgResponseMinutes();

    @Select("SELECT fault_type as faultType, COUNT(*) as count FROM repair_order WHERE is_deleted = 0 GROUP BY fault_type")
    List<Map<String, Object>> getFaultTypeStatistics();

    @Select("SELECT status, COUNT(*) as count FROM repair_order WHERE is_deleted = 0 GROUP BY status")
    List<Map<String, Object>> getOrderStatusStatistics();

    @Select("SELECT DATE(create_time) as date, COUNT(*) as count FROM repair_order WHERE create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> getOrderTrend(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT TIMESTAMPDIFF(MINUTE, create_time, completed_time) FROM repair_order WHERE status = 4 AND completed_time IS NOT NULL AND is_deleted = 0")
    List<Integer> getRepairDurationMinutes();
}
