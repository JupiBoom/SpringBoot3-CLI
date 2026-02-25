package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoomItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 直播间商品关联表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface LiveRoomItemMapper extends BaseMapper<LiveRoomItem> {

    /**
     * 根据直播间ID查询商品列表
     */
    @Select("SELECT * FROM live_room_item WHERE room_id = #{roomId} AND is_deleted = 0 ORDER BY sort_order ASC, create_time DESC")
    List<LiveRoomItem> selectByRoomId(@Param("roomId") Long roomId);

    /**
     * 更新商品销售数据
     */
    @Update("UPDATE live_room_item SET sales_count = sales_count + #{count}, sales_amount = sales_amount + #{amount}, updater_id = #{updaterId}, update_time = NOW() WHERE room_id = #{roomId} AND item_id = #{itemId}")
    int updateSalesData(@Param("roomId") Long roomId, @Param("itemId") Long itemId, @Param("count") Integer count, @Param("amount") BigDecimal amount, @Param("updaterId") Long updaterId);

    /**
     * 查询直播间商品销售排行榜
     */
    @Select("SELECT * FROM live_room_item WHERE room_id = #{roomId} AND is_deleted = 0 ORDER BY sales_count DESC, sales_amount DESC LIMIT #{limit}")
    List<LiveRoomItem> selectTopSellingItems(@Param("roomId") Long roomId, @Param("limit") Integer limit);
}
