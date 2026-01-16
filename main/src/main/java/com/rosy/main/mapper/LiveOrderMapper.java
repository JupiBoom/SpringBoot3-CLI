package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播订单表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Mapper
public interface LiveOrderMapper extends BaseMapper<LiveOrder> {

    @Select("SELECT product_id, product_name, SUM(quantity) as totalQuantity, SUM(total_amount) as totalAmount, COUNT(*) as orderCount FROM live_order WHERE live_room_id = #{liveRoomId} AND status = 1 GROUP BY product_id, product_name ORDER BY totalAmount DESC LIMIT 10")
    List<Map<String, Object>> getProductSalesRanking(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT COUNT(*) as totalOrders, SUM(total_amount) as totalSales, COUNT(CASE WHEN status = 1 THEN 1 END) as paidOrders, SUM(CASE WHEN status = 1 THEN total_amount ELSE 0 END) as paidSales FROM live_order WHERE live_room_id = #{liveRoomId}")
    Map<String, Object> getOrderSalesSummary(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT CASE WHEN #{productClickCount} > 0 THEN #{orderCount} / #{productClickCount} * 100 ELSE 0 END as conversionRate")
    BigDecimal calculateConversionRate(@Param("orderCount") Integer orderCount, @Param("productClickCount") Integer productClickCount);
}