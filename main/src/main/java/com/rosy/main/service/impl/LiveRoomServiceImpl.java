package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.*;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveRoomItem;
import com.rosy.main.domain.vo.LiveRoomItemVO;
import com.rosy.main.domain.vo.LiveRoomStatsVO;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.ILiveOrderService;
import com.rosy.main.service.ILiveRoomItemService;
import com.rosy.main.service.ILiveRoomService;
import com.rosy.main.service.ILiveViewerStatsService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    @Resource
    private ILiveRoomItemService liveRoomItemService;

    @Resource
    private ILiveOrderService liveOrderService;

    @Resource
    private ILiveViewerStatsService liveViewerStatsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addLiveRoom(LiveRoomAddRequest request, Long creatorId) {
        LiveRoom liveRoom = new LiveRoom();
        BeanUtils.copyProperties(request, liveRoom);
        liveRoom.setStatus((byte) 0);
        liveRoom.setViewerCount(0);
        liveRoom.setTotalViewers(0);
        liveRoom.setLikeCount(0);
        liveRoom.setOrderCount(0);
        liveRoom.setSalesAmount(BigDecimal.ZERO);
        liveRoom.setCreatorId(creatorId);
        boolean result = this.save(liveRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return liveRoom.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLiveRoom(Long roomId) {
        return this.removeById(roomId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLiveRoom(LiveRoomUpdateRequest request, Long updaterId) {
        LiveRoom liveRoom = new LiveRoom();
        BeanUtils.copyProperties(request, liveRoom);
        liveRoom.setUpdaterId(updaterId);
        return this.updateById(liveRoom);
    }

    @Override
    public LiveRoomVO getLiveRoomById(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            return null;
        }
        return convertToVO(liveRoom);
    }

    @Override
    public Page<LiveRoomVO> listLiveRoomByPage(LiveRoomQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<LiveRoom> page = new Page<>(current, size);
        LambdaQueryWrapper<LiveRoom> queryWrapper = buildQueryWrapper(request);
        Page<LiveRoom> liveRoomPage = this.page(page, queryWrapper);
        List<LiveRoomVO> voList = liveRoomPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        Page<LiveRoomVO> voPage = new Page<>(liveRoomPage.getCurrent(), liveRoomPage.getSize(), liveRoomPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLiveRoomStatus(LiveRoomStatusRequest request, Long updaterId) {
        return baseMapper.updateStatus(request.getRoomId(), request.getStatus(), updaterId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startLive(Long roomId, Long updaterId) {
        LiveRoom liveRoom = this.getById(roomId);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(liveRoom.getStatus() == 1, ErrorCode.OPERATION_ERROR, "直播间已在直播中");
        
        liveRoom.setStatus((byte) 1);
        liveRoom.setStartTime(LocalDateTime.now());
        liveRoom.setEndTime(null);
        liveRoom.setUpdaterId(updaterId);
        return this.updateById(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean endLive(Long roomId, Long updaterId) {
        LiveRoom liveRoom = this.getById(roomId);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);
        ThrowUtils.throwIf(liveRoom.getStatus() != 1, ErrorCode.OPERATION_ERROR, "直播间未在直播中");
        
        liveRoom.setStatus((byte) 2);
        liveRoom.setEndTime(LocalDateTime.now());
        liveRoom.setUpdaterId(updaterId);
        return this.updateById(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean switchCurrentItem(LiveRoomSwitchItemRequest request, Long updaterId) {
        return baseMapper.updateCurrentItem(request.getRoomId(), request.getItemId(), updaterId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementViewerCount(Long roomId, Integer count, Long updaterId) {
        return baseMapper.incrementViewerCount(roomId, count, updaterId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decrementViewerCount(Long roomId, Integer count, Long updaterId) {
        return baseMapper.decrementViewerCount(roomId, count, updaterId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incrementLikeCount(Long roomId, Integer count, Long updaterId) {
        return baseMapper.incrementLikeCount(roomId, count, updaterId) > 0;
    }

    @Override
    public LiveRoomStatsVO getLiveRoomStats(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);

        LiveRoomStatsVO statsVO = new LiveRoomStatsVO();
        statsVO.setRoomId(roomId);
        statsVO.setRoomTitle(liveRoom.getTitle());

        // 基础数据
        statsVO.setViewerCount(liveRoom.getViewerCount());
        statsVO.setTotalViewers(liveRoom.getTotalViewers());
        statsVO.setLikeCount(liveRoom.getLikeCount());
        statsVO.setOrderCount(liveRoom.getOrderCount());
        statsVO.setSalesAmount(liveRoom.getSalesAmount());

        // 订单相关数据
        Long paidOrderCount = liveOrderService.getPaidOrderCount(roomId);
        statsVO.setPaidOrderCount(paidOrderCount != null ? paidOrderCount.intValue() : 0);

        BigDecimal totalSales = liveOrderService.getTotalSalesAmount(roomId);
        statsVO.setSalesAmount(totalSales != null ? totalSales : BigDecimal.ZERO);

        // 转化率计算
        Integer totalViewers = liveRoom.getTotalViewers();
        Integer orderCount = statsVO.getOrderCount();
        Integer paidCount = statsVO.getPaidOrderCount();

        if (totalViewers != null && totalViewers > 0 && orderCount != null) {
            BigDecimal viewToOrderRate = BigDecimal.valueOf(orderCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalViewers), 2, RoundingMode.HALF_UP);
            statsVO.setViewToOrderRate(viewToOrderRate + "%");
        } else {
            statsVO.setViewToOrderRate("0.00%");
        }

        if (orderCount != null && orderCount > 0 && paidCount != null) {
            BigDecimal orderToPayRate = BigDecimal.valueOf(paidCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(orderCount), 2, RoundingMode.HALF_UP);
            statsVO.setOrderToPayRate(orderToPayRate + "%");
        } else {
            statsVO.setOrderToPayRate("0.00%");
        }

        if (totalViewers != null && totalViewers > 0 && paidCount != null) {
            BigDecimal overallRate = BigDecimal.valueOf(paidCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(totalViewers), 2, RoundingMode.HALF_UP);
            statsVO.setOverallConversionRate(overallRate + "%");
        } else {
            statsVO.setOverallConversionRate("0.00%");
        }

        // 客单价
        if (paidCount != null && paidCount > 0 && totalSales != null) {
            BigDecimal avgOrderValue = totalSales.divide(BigDecimal.valueOf(paidCount), 2, RoundingMode.HALF_UP);
            statsVO.setAvgOrderValue(avgOrderValue);
        } else {
            statsVO.setAvgOrderValue(BigDecimal.ZERO);
        }

        // 商品销售排行榜
        List<LiveRoomItemVO> topItems = liveRoomItemService.getTopSellingItems(roomId, 10);
        statsVO.setTopSellingItems(topItems);

        // 观众趋势数据（留存曲线）
        List<Map<String, Object>> retentionCurve = liveViewerStatsService.generateRetentionCurve(roomId);
        statsVO.setViewerTrend(retentionCurve);

        // 销售趋势数据
        List<Map<String, Object>> salesTrend = liveOrderService.getMinuteOrderStats(roomId);
        statsVO.setSalesTrend(salesTrend);

        // 停留时长分布
        Map<String, Object> stayDistribution = liveViewerStatsService.getStayDurationDistribution(roomId);
        statsVO.setStayDurationDistribution(stayDistribution);

        // 平均停留时长
        Double avgStayDuration = liveViewerStatsService.getAvgStayDuration(roomId);
        statsVO.setAvgStayDuration(avgStayDuration != null ? avgStayDuration : 0.0);

        return statsVO;
    }

    @Override
    public List<LiveRoomVO> listLiveRooms(LiveRoomQueryRequest request) {
        LambdaQueryWrapper<LiveRoom> queryWrapper = buildQueryWrapper(request);
        List<LiveRoom> list = this.list(queryWrapper);
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public boolean updateRoomSalesData(Long roomId, Integer orderCount, BigDecimal salesAmount, Long updaterId) {
        return baseMapper.updateSalesData(roomId, orderCount, salesAmount, updaterId) > 0;
    }

    private LambdaQueryWrapper<LiveRoom> buildQueryWrapper(LiveRoomQueryRequest request) {
        LambdaQueryWrapper<LiveRoom> queryWrapper = new LambdaQueryWrapper<>();
        if (request.getId() != null) {
            queryWrapper.eq(LiveRoom::getId, request.getId());
        }
        if (StrUtil.isNotBlank(request.getTitle())) {
            queryWrapper.like(LiveRoom::getTitle, request.getTitle());
        }
        if (request.getStreamerId() != null) {
            queryWrapper.eq(LiveRoom::getStreamerId, request.getStreamerId());
        }
        if (request.getStatus() != null) {
            queryWrapper.eq(LiveRoom::getStatus, request.getStatus());
        }
        if (StrUtil.isNotBlank(request.getKeyword())) {
            queryWrapper.and(qw -> qw.like(LiveRoom::getTitle, request.getKeyword())
                    .or()
                    .like(LiveRoom::getStreamerName, request.getKeyword()));
        }
        queryWrapper.orderByDesc(LiveRoom::getCreateTime);
        return queryWrapper;
    }

    private LiveRoomVO convertToVO(LiveRoom liveRoom) {
        LiveRoomVO vo = new LiveRoomVO();
        BeanUtils.copyProperties(liveRoom, vo);
        // 设置状态文本
        switch (liveRoom.getStatus()) {
            case 0:
                vo.setStatusText("未开始");
                break;
            case 1:
                vo.setStatusText("直播中");
                break;
            case 2:
                vo.setStatusText("已结束");
                break;
            case 3:
                vo.setStatusText("已禁播");
                break;
            default:
                vo.setStatusText("未知");
        }
        // 加载当前讲解商品
        if (liveRoom.getCurrentItemId() != null) {
            LiveRoomItemVO currentItem = liveRoomItemService.getLiveRoomItemById(liveRoom.getCurrentItemId());
            vo.setCurrentItem(currentItem);
        }
        return vo;
    }
}
