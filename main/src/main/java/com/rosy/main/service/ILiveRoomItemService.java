package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveRoomItemAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomItemUpdateRequest;
import com.rosy.main.domain.entity.LiveRoomItem;
import com.rosy.main.domain.vo.LiveRoomItemVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 直播间商品关联表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface ILiveRoomItemService extends IService<LiveRoomItem> {

    /**
     * 添加商品到直播间
     */
    Long addLiveRoomItem(LiveRoomItemAddRequest request, Long creatorId);

    /**
     * 删除直播间商品
     */
    boolean deleteLiveRoomItem(Long id);

    /**
     * 更新直播间商品
     */
    boolean updateLiveRoomItem(LiveRoomItemUpdateRequest request, Long updaterId);

    /**
     * 根据ID获取直播间商品
     */
    LiveRoomItemVO getLiveRoomItemById(Long id);

    /**
     * 获取直播间的商品列表
     */
    List<LiveRoomItemVO> listItemsByRoomId(Long roomId);

    /**
     * 获取直播间商品销售排行榜
     */
    List<LiveRoomItemVO> getTopSellingItems(Long roomId, Integer limit);

    /**
     * 更新商品销售数据
     */
    boolean updateItemSalesData(Long roomId, Long itemId, Integer count, BigDecimal amount, Long updaterId);

    /**
     * 获取当前讲解商品
     */
    LiveRoomItemVO getCurrentItem(Long roomId);
}
