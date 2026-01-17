package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 报修工单Mapper接口
 */
@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {

    @Select("SELECT AVG(response_time) FROM repair_order WHERE response_time IS NOT NULL AND create_time >= #{startTime}")
    BigDecimal avgResponseTime(@Param("startTime") LocalDateTime startTime);

    @Select("SELECT fault_type, COUNT(*) as count FROM repair_order WHERE create_time >= #{startTime} GROUP BY fault_type ORDER BY count DESC")
    List<Map<String, Object>> faultTypeStatistics(@Param("startTime") LocalDateTime startTime);

    @Select("SELECT DATE(create_time) as date, COUNT(*) as count FROM repair_order WHERE create_time >= #{startTime} GROUP BY DATE(create_time) ORDER BY date DESC")
    List<Map<String, Object>> orderTrendStatistics(@Param("startTime") LocalDateTime startTime);

    @Select("SELECT status, COUNT(*) as count FROM repair_order WHERE create_time >= #{startTime} GROUP BY status")
    List<Map<String, Object>> statusStatistics(@Param("startTime") LocalDateTime startTime);

    @Select("SELECT equipment_type, COUNT(*) as count FROM repair_order WHERE create_time >= #{startTime} GROUP BY equipment_type ORDER BY count DESC")
    List<Map<String, Object>> equipmentTypeStatistics(@Param("startTime") LocalDateTime startTime);
}
