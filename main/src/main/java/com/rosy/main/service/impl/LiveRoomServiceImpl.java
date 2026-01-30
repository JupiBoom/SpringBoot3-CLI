package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.live.*;
import com.rosy.main.domain.entity.*;
import com.rosy.main.domain.vo.*;
import com.rosy.main.mapper.*;
import com.rosy.main.service.ILiveRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    private final LiveProductMapper liveProductMapper;
    private final LiveRoomStatsMapper liveRoomStatsMapper;
    private final LiveProductStatsMapper liveProductStatsMapper;
    private final LiveAudienceRetentionMapper liveAudienceRetentionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRoom(LiveRoomCreateRequest request) {
        LiveRoom liveRoom = BeanUtil.copyProperties(request, LiveRoom.class);
        liveRoom.setStatus((byte) 0);
        this.save(liveRoom);

        LiveRoomStats stats = new LiveRoomStats();
        stats.setRoomId(liveRoom.getId());
        stats.setTotalViewers(0L);
        stats.setPeakViewers(0);
        stats.setCurrentViewers(0);
        stats.setTotalOrders(0);
        stats.setTotalSales(BigDecimal.ZERO);
        stats.setTotalLikes(0L);
        stats.setTotalComments(0);
        stats.setTotalShares(0);
        liveRoomStatsMapper.insert(stats);

        return liveRoom.getId();
    }

    @Override
    public boolean updateRoom(LiveRoomUpdateRequest request) {
        LiveRoom liveRoom = this.getById(request.getId());
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        BeanUtil.copyProperties(request, liveRoom, "id");
        return this.updateById(liveRoom);
    }

    @Override
    public boolean updateRoomStatus(LiveRoomStatusUpdateRequest request) {
        LiveRoom liveRoom = this.getById(request.getRoomId());
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        liveRoom.setStatus(request.getStatus());
        return this.updateById(liveRoom);
    }

    @Override
    public LiveRoomVO getRoomDetail(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        return Optional.ofNullable(liveRoom)
                .map(room -> BeanUtil.copyProperties(room, LiveRoomVO.class))
                .orElse(null);
    }

    @Override
    public LiveRoomWithStatsVO getRoomWithStats(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            return null;
        }

        LiveRoomWithStatsVO result = new LiveRoomWithStatsVO();
        result.setRoomInfo(BeanUtil.copyProperties(liveRoom, LiveRoomVO.class));

        LambdaQueryWrapper<LiveRoomStats> statsWrapper = new LambdaQueryWrapper<>();
        statsWrapper.eq(LiveRoomStats::getRoomId, roomId);
        LiveRoomStats stats = liveRoomStatsMapper.selectOne(statsWrapper);
        if (stats != null) {
            result.setStats(BeanUtil.copyProperties(stats, LiveRoomStatsVO.class));
        }

        LambdaQueryWrapper<LiveProduct> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(LiveProduct::getRoomId, roomId)
                .orderByAsc(LiveProduct::getSortOrder);
        List<LiveProduct> products = liveProductMapper.selectList(productWrapper);
        if (CollUtil.isNotEmpty(products)) {
            result.setProducts(products.stream()
                    .map(this::convertToLiveProductVO)
                    .collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public List<LiveRoomVO> getRoomList(LiveRoomQueryRequest request) {
        LambdaQueryWrapper<LiveRoom> wrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(wrapper, request.getId(), LiveRoom::getId);
        QueryWrapperUtil.addCondition(wrapper, request.getRoomName(), LiveRoom::getRoomName);
        QueryWrapperUtil.addCondition(wrapper, request.getAnchorId(), LiveRoom::getAnchorId);
        QueryWrapperUtil.addCondition(wrapper, request.getAnchorName(), LiveRoom::getAnchorName);
        QueryWrapperUtil.addCondition(wrapper, request.getStatus(), LiveRoom::getStatus);

        QueryWrapperUtil.addSortCondition(wrapper,
                request.getSortField(),
                request.getSortOrder(),
                LiveRoom::getId);

        List<LiveRoom> rooms = this.list(wrapper);
        return rooms.stream()
                .map(room -> BeanUtil.copyProperties(room, LiveRoomVO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startLive(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (liveRoom.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播间已在直播中");
        }

        liveRoom.setStatus((byte) 1);
        liveRoom.setActualStartTime(LocalDateTime.now());
        return this.updateById(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean endLive(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (liveRoom.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播间未在直播中");
        }

        liveRoom.setStatus((byte) 2);
        liveRoom.setEndTime(LocalDateTime.now());
        return this.updateById(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean switchExplainingProduct(Long roomId, Long productId) {
        LambdaQueryWrapper<LiveProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveProduct::getRoomId, roomId);
        List<LiveProduct> products = liveProductMapper.selectList(wrapper);

        for (LiveProduct product : products) {
            if (product.getProductId().equals(productId)) {
                product.setIsExplaining((byte) 1);
            } else {
                product.setIsExplaining((byte) 0);
            }
            liveProductMapper.updateById(product);
        }

        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom != null) {
            liveRoom.setCurrentProductId(productId);
            this.updateById(liveRoom);
        }

        return true;
    }

    @Override
    public LiveRoomConversionStatsVO getConversionStats(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }

        LambdaQueryWrapper<LiveRoomStats> statsWrapper = new LambdaQueryWrapper<>();
        statsWrapper.eq(LiveRoomStats::getRoomId, roomId);
        LiveRoomStats stats = liveRoomStatsMapper.selectOne(statsWrapper);

        if (stats == null) {
            return null;
        }

        LiveRoomConversionStatsVO result = new LiveRoomConversionStatsVO();
        result.setRoomId(roomId);
        result.setTotalViewers(stats.getTotalViewers());
        result.setTotalOrders(stats.getTotalOrders());
        result.setTotalSales(stats.getTotalSales());

        if (stats.getTotalViewers() > 0) {
            BigDecimal orderRate = BigDecimal.valueOf(stats.getTotalOrders())
                    .divide(BigDecimal.valueOf(stats.getTotalViewers()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            result.setOrderConversionRate(orderRate);
        } else {
            result.setOrderConversionRate(BigDecimal.ZERO);
        }

        if (stats.getTotalOrders() > 0) {
            result.setAverageOrderValue(stats.getTotalSales()
                    .divide(BigDecimal.valueOf(stats.getTotalOrders()), 2, RoundingMode.HALF_UP));
        } else {
            result.setAverageOrderValue(BigDecimal.ZERO);
        }

        LambdaQueryWrapper<LiveProductStats> productStatsWrapper = new LambdaQueryWrapper<>();
        productStatsWrapper.eq(LiveProductStats::getRoomId, roomId);
        List<LiveProductStats> productStats = liveProductStatsMapper.selectList(productStatsWrapper);
        int totalClickCount = productStats.stream()
                .mapToInt(LiveProductStats::getClickCount)
                .sum();
        result.setProductClickCount(totalClickCount);

        if (stats.getTotalViewers() > 0) {
            BigDecimal clickRate = BigDecimal.valueOf(totalClickCount)
                    .divide(BigDecimal.valueOf(stats.getTotalViewers()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            result.setProductClickRate(clickRate);
        } else {
            result.setProductClickRate(BigDecimal.ZERO);
        }

        return result;
    }

    @Override
    public List<LiveRetentionDataVO> getRetentionCurve(Long roomId) {
        LambdaQueryWrapper<LiveAudienceRetention> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveAudienceRetention::getRoomId, roomId)
                .orderByAsc(LiveAudienceRetention::getMinuteOffset);
        List<LiveAudienceRetention> retentionList = liveAudienceRetentionMapper.selectList(wrapper);

        return retentionList.stream()
                .map(r -> {
                    LiveRetentionDataVO vo = new LiveRetentionDataVO();
                    vo.setRecordTime(r.getRecordTime());
                    vo.setMinuteOffset(r.getMinuteOffset());
                    vo.setViewerCount(r.getViewerCount());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    private LiveProductVO convertToLiveProductVO(LiveProduct product) {
        LiveProductVO vo = BeanUtil.copyProperties(product, LiveProductVO.class);
        if (StrUtil.isNotBlank(product.getSellingPoints())) {
            vo.setSellingPoints(JSON.parseArray(product.getSellingPoints(), String.class));
        }
        return vo;
    }
}
