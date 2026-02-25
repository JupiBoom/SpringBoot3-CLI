package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播间订单表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface LiveOrderMapper extends BaseMapper<LiveOrder> {

    /**
     * 查询直播间订单总数
     */
    @Select("SELECT COUNT(*) FROM live_order WHERE room_id = #{roomId} AND is_deleted = 0")
    Long countByRoomId(@Param("roomId") Long roomId);

    /**
     * 查询直播间订单总金额
     */
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM live_order WHERE room_id = #{roomId} AND status = 1 AND is_deleted = 0")
    BigDecimal sumAmountByRoomId(@Param("roomId") Long roomId);

    /**
     * 查询直播间已支付订单数
     */
    @Select("SELECT COUNT(*) FROM live_order WHERE room_id = #{roomId} AND status = 1 AND is_deleted = 0")
    Long countPaidOrdersByRoomId(@Param("roomId") Long roomId);

    /**
     * 查询直播间各商品销售统计
     */
    @Select("SELECT item_id, COUNT(*) as order_count, SUM(quantity) as total_quantity, SUM(total_amount) as total_amount " +
            "FROM live_order WHERE room_id = #{roomId} AND status = 1 AND is_deleted = 0 " +
            "GROUP BY item_id ORDER BY total_amount DESC")
    List<Map<String, Object>> selectItemSalesStats(@Param("roomId") Long roomId);

    /**
     * 查询直播间每分钟的订单数据（用于留存曲线）
     */
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d %H:%i') as time_slot, COUNT(*) as order_count, SUM(total_amount) as sales_amount " +
            "FROM live_order WHERE room_id = #{roomId} AND create_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 " +
            "GROUP BY time_slot ORDER BY time_slot")
    List<Map<String, Object>> selectMinuteOrderStats(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
