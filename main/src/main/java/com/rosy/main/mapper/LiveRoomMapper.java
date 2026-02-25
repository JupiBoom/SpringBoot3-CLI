package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoom;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 直播间表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface LiveRoomMapper extends BaseMapper<LiveRoom> {

    /**
     * 更新直播间状态
     */
    @Update("UPDATE live_room SET status = #{status}, updater_id = #{updaterId}, update_time = NOW() WHERE id = #{roomId}")
    int updateStatus(@Param("roomId") Long roomId, @Param("status") Byte status, @Param("updaterId") Long updaterId);

    /**
     * 增加观众人数
     */
    @Update("UPDATE live_room SET viewer_count = viewer_count + #{count}, total_viewers = total_viewers + #{count}, updater_id = #{updaterId}, update_time = NOW() WHERE id = #{roomId}")
    int incrementViewerCount(@Param("roomId") Long roomId, @Param("count") Integer count, @Param("updaterId") Long updaterId);

    /**
     * 减少观众人数
     */
    @Update("UPDATE live_room SET viewer_count = GREATEST(0, viewer_count - #{count}), updater_id = #{updaterId}, update_time = NOW() WHERE id = #{roomId}")
    int decrementViewerCount(@Param("roomId") Long roomId, @Param("count") Integer count, @Param("updaterId") Long updaterId);

    /**
     * 增加点赞数
     */
    @Update("UPDATE live_room SET like_count = like_count + #{count}, updater_id = #{updaterId}, update_time = NOW() WHERE id = #{roomId}")
    int incrementLikeCount(@Param("roomId") Long roomId, @Param("count") Integer count, @Param("updaterId") Long updaterId);

    /**
     * 更新销售数据
     */
    @Update("UPDATE live_room SET order_count = order_count + #{orderCount}, sales_amount = sales_amount + #{salesAmount}, updater_id = #{updaterId}, update_time = NOW() WHERE id = #{roomId}")
    int updateSalesData(@Param("roomId") Long roomId, @Param("orderCount") Integer orderCount, @Param("salesAmount") java.math.BigDecimal salesAmount, @Param("updaterId") Long updaterId);

    /**
     * 设置当前讲解商品
     */
    @Update("UPDATE live_room SET current_item_id = #{itemId}, updater_id = #{updaterId}, update_time = NOW() WHERE id = #{roomId}")
    int updateCurrentItem(@Param("roomId") Long roomId, @Param("itemId") Long itemId, @Param("updaterId") Long updaterId);
}
