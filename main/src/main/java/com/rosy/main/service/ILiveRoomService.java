package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.*;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomStatsVO;
import com.rosy.main.domain.vo.LiveRoomVO;

import java.util.List;

/**
 * <p>
 * 直播间表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface ILiveRoomService extends IService<LiveRoom> {

    /**
     * 创建直播间
     */
    Long addLiveRoom(LiveRoomAddRequest request, Long creatorId);

    /**
     * 删除直播间
     */
    boolean deleteLiveRoom(Long roomId);

    /**
     * 更新直播间
     */
    boolean updateLiveRoom(LiveRoomUpdateRequest request, Long updaterId);

    /**
     * 根据ID获取直播间
     */
    LiveRoomVO getLiveRoomById(Long roomId);

    /**
     * 分页查询直播间列表
     */
    Page<LiveRoomVO> listLiveRoomByPage(LiveRoomQueryRequest request);

    /**
     * 更新直播间状态
     */
    boolean updateLiveRoomStatus(LiveRoomStatusRequest request, Long updaterId);

    /**
     * 开始直播
     */
    boolean startLive(Long roomId, Long updaterId);

    /**
     * 结束直播
     */
    boolean endLive(Long roomId, Long updaterId);

    /**
     * 切换讲解商品
     */
    boolean switchCurrentItem(LiveRoomSwitchItemRequest request, Long updaterId);

    /**
     * 增加观众人数
     */
    boolean incrementViewerCount(Long roomId, Integer count, Long updaterId);

    /**
     * 减少观众人数
     */
    boolean decrementViewerCount(Long roomId, Integer count, Long updaterId);

    /**
     * 增加点赞数
     */
    boolean incrementLikeCount(Long roomId, Integer count, Long updaterId);

    /**
     * 获取直播间统计数据
     */
    LiveRoomStatsVO getLiveRoomStats(Long roomId);

    /**
     * 获取直播间列表（不分页）
     */
    List<LiveRoomVO> listLiveRooms(LiveRoomQueryRequest request);

    /**
     * 更新直播间销售数据
     */
    boolean updateRoomSalesData(Long roomId, Integer orderCount, java.math.BigDecimal salesAmount, Long updaterId);
}
