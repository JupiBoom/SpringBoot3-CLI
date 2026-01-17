package com.rosy.repair.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.repair.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

    @Select("SELECT fault_type, COUNT(*) as count FROM repair_order WHERE is_deleted = 0 AND create_time BETWEEN #{startTime} AND #{endTime} GROUP BY fault_type")
    List<Map<String, Object>> selectFaultTypeAnalysis(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT device_type, COUNT(*) as count FROM repair_order WHERE is_deleted = 0 AND create_time BETWEEN #{startTime} AND #{endTime} GROUP BY device_type")
    List<Map<String, Object>> selectDeviceTypeAnalysis(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT priority, COUNT(*) as count FROM repair_order WHERE is_deleted = 0 AND create_time BETWEEN #{startTime} AND #{endTime} GROUP BY priority")
    List<Map<String, Object>> selectPriorityAnalysis(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Select("SELECT AVG(TIMESTAMPDIFF(SECOND, create_time, assign_time)) as avg_response_time FROM repair_order WHERE is_deleted = 0 AND assign_time IS NOT NULL AND create_time BETWEEN #{startTime} AND #{endTime}")
    Long selectAvgResponseTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}