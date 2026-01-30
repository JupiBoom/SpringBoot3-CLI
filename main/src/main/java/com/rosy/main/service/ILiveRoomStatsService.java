package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomStats;
import com.rosy.main.domain.vo.LiveRoomStatsVO;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 直播间数据统计表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
public interface ILiveRoomStatsService extends IService<LiveRoomStats> {

    /**
     * 获取直播间数据统计视图对象
     * @param liveRoomStats 直播间数据统计实体
     * @return 直播间数据统计视图对象
     */
    LiveRoomStatsVO getLiveRoomStatsVO(LiveRoomStats liveRoomStats);

    /**
     * 获取直播间统计数据列表
     * @param liveRoomId 直播间ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<LiveRoomStatsVO> getStatsByDateRange(Long liveRoomId, LocalDate startDate, LocalDate endDate);

    /**
     * 分页获取直播间统计数据列表
     * @param liveRoomId 直播间ID
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    Page<LiveRoomStatsVO> pageStatsByLiveRoomId(Long liveRoomId, long current, long size);

    /**
     * 生成每日统计数据
     * @param liveRoomId 直播间ID
     * @param statsDate 统计日期
     * @return 是否成功
     */
    boolean generateDailyStats(Long liveRoomId, LocalDate statsDate);

    /**
     * 更新直播间观众数据
     * @param liveRoomId 直播间ID
     * @param totalViewers 总观众人数
     * @param peakViewers 峰值观众人数
     * @return 是否成功
     */
    boolean updateViewerStats(Long liveRoomId, Integer totalViewers, Integer peakViewers);

    /**
     * 更新直播间销售数据
     * @param liveRoomId 直播间ID
     * @param totalOrders 总订单数
     * @param totalSales 总销售额
     * @return 是否成功
     */
    boolean updateSalesStats(Long liveRoomId, Integer totalOrders, java.math.BigDecimal totalSales);
}