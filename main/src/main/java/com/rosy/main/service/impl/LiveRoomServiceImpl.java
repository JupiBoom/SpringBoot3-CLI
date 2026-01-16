package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.live.LiveRoomAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.live.LiveRoomStatusUpdateRequest;
import com.rosy.main.domain.dto.live.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveData;
import com.rosy.main.domain.entity.LiveOrder;
import com.rosy.main.domain.entity.LiveProduct;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveProductVO;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveStatsVO;
import com.rosy.main.mapper.LiveDataMapper;
import com.rosy.main.mapper.LiveOrderMapper;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.ILiveProductService;
import com.rosy.main.service.ILiveRoomService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    private final ILiveProductService liveProductService;
    private final LiveDataMapper liveDataMapper;
    private final LiveOrderMapper liveOrderMapper;

    public LiveRoomServiceImpl(ILiveProductService liveProductService,
                                LiveDataMapper liveDataMapper,
                                LiveOrderMapper liveOrderMapper) {
        this.liveProductService = liveProductService;
        this.liveDataMapper = liveDataMapper;
        this.liveOrderMapper = liveOrderMapper;
    }

    @Override
    public LiveRoomVO getLiveRoomVO(LiveRoom liveRoom) {
        if (liveRoom == null) {
            return null;
        }
        LiveRoomVO liveRoomVO = BeanUtil.copyProperties(liveRoom, LiveRoomVO.class);
        
        String statusText = switch (liveRoom.getStatus()) {
            case 0 -> "未开播";
            case 1 -> "直播中";
            case 2 -> "已结束";
            case 3 -> "暂停";
            default -> "未知";
        };
        liveRoomVO.setStatusText(statusText);
        
        List<LiveProductVO> products = liveProductService.listLiveProducts(liveRoom.getId());
        liveRoomVO.setProducts(products);
        
        LiveStatsVO stats = getLiveRoomStats(liveRoom.getId());
        liveRoomVO.setStats(stats);
        
        return liveRoomVO;
    }

    @Override
    public LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<LiveRoom> queryWrapper = new LambdaQueryWrapper<>();
        
        QueryWrapperUtil.addCondition(queryWrapper, request.getId(), LiveRoom::getId);
        QueryWrapperUtil.addLikeCondition(queryWrapper, request.getTitle(), LiveRoom::getTitle);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), LiveRoom::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, request.getAnchorId(), LiveRoom::getAnchorId);
        QueryWrapperUtil.addLikeCondition(queryWrapper, request.getAnchorName(), LiveRoom::getAnchorName);
        QueryWrapperUtil.addBetweenCondition(queryWrapper, request.getStartTimeStart(), request.getStartTimeEnd(), LiveRoom::getStartTime);
        QueryWrapperUtil.addBetweenCondition(queryWrapper, request.getCreateTimeStart(), request.getCreateTimeEnd(), LiveRoom::getCreateTime);
        
        QueryWrapperUtil.addSortCondition(queryWrapper,
                request.getSortField(),
                request.getSortOrder(),
                LiveRoom::getCreateTime);
        
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LiveRoomVO createLiveRoom(LiveRoomAddRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        
        LiveRoom liveRoom = BeanUtil.copyProperties(request, LiveRoom.class);
        liveRoom.setStatus((byte) 0);
        liveRoom.setViewerCount(0);
        liveRoom.setPeakViewerCount(0);
        liveRoom.setCreatorId(1L);
        liveRoom.setUpdaterId(1L);
        
        if (!this.save(liveRoom)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建直播间失败");
        }
        
        return getLiveRoomVO(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LiveRoomVO updateLiveRoom(LiveRoomUpdateRequest request) {
        if (request == null || request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        
        LiveRoom liveRoom = this.getById(request.getId());
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        
        if (liveRoom.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播中无法修改直播间信息");
        }
        
        if (StrUtil.isNotBlank(request.getTitle())) {
            liveRoom.setTitle(request.getTitle());
        }
        if (StrUtil.isNotBlank(request.getCoverImage())) {
            liveRoom.setCoverImage(request.getCoverImage());
        }
        if (StrUtil.isNotBlank(request.getAnchorName())) {
            liveRoom.setAnchorName(request.getAnchorName());
        }
        if (request.getExpectedDuration() != null) {
            liveRoom.setExpectedDuration(request.getExpectedDuration());
        }
        if (StrUtil.isNotBlank(request.getDescription())) {
            liveRoom.setDescription(request.getDescription());
        }
        liveRoom.setUpdaterId(1L);
        
        if (!this.updateById(liveRoom)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新直播间失败");
        }
        
        return getLiveRoomVO(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLiveRoom(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间ID不能为空");
        }
        
        LiveRoom liveRoom = this.getById(liveRoomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        
        if (liveRoom.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播中无法删除直播间");
        }
        
        return this.removeById(liveRoomId);
    }

    @Override
    public LiveRoomVO getLiveRoomDetail(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间ID不能为空");
        }
        
        LiveRoom liveRoom = this.getById(liveRoomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        
        return getLiveRoomVO(liveRoom);
    }

    @Override
    public Page<LiveRoomVO> listLiveRooms(LiveRoomQueryRequest request, int pageNum, int pageSize) {
        LambdaQueryWrapper<LiveRoom> queryWrapper = getQueryWrapper(request);
        Page<LiveRoom> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        
        Page<LiveRoomVO> resultPage = new Page<>(pageNum, pageSize, page.getTotal());
        List<LiveRoomVO> records = page.getRecords().stream()
                .map(this::getLiveRoomVO)
                .collect(Collectors.toList());
        resultPage.setRecords(records);
        
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LiveRoomVO updateLiveRoomStatus(LiveRoomStatusUpdateRequest request) {
        if (request == null || request.getLiveRoomId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        
        LiveRoom liveRoom = this.getById(request.getLiveRoomId());
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        
        Byte currentStatus = liveRoom.getStatus();
        Byte targetStatus = request.getTargetStatus();
        
        if (Objects.equals(currentStatus, targetStatus)) {
            return getLiveRoomVO(liveRoom);
        }
        
        if (targetStatus == 1 && currentStatus == 0) {
            liveRoom.setStatus((byte) 1);
            liveRoom.setStartTime(LocalDateTime.now());
        } else if (targetStatus == 3 && currentStatus == 1) {
            liveRoom.setStatus((byte) 3);
        } else if (targetStatus == 1 && currentStatus == 3) {
            liveRoom.setStatus((byte) 1);
        } else if (targetStatus == 2 && currentStatus == 1) {
            liveRoom.setStatus((byte) 2);
            liveRoom.setEndTime(LocalDateTime.now());
        } else if (targetStatus == 2 && currentStatus == 3) {
            liveRoom.setStatus((byte) 2);
            liveRoom.setEndTime(LocalDateTime.now());
        } else {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不支持的状态转换");
        }
        
        if (request.getCurrentProductId() != null) {
            liveRoom.setCurrentProductId(request.getCurrentProductId());
        }
        
        liveRoom.setUpdaterId(1L);
        
        if (!this.updateById(liveRoom)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新直播间状态失败");
        }
        
        return getLiveRoomVO(liveRoom);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean switchCurrentProduct(Long liveRoomId, Long productId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间ID不能为空");
        }
        
        LiveRoom liveRoom = this.getById(liveRoomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        
        if (liveRoom.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只有直播中才能切换讲解商品");
        }
        
        if (productId != null) {
            LiveProduct liveProduct = liveProductService.getById(productId);
            if (liveProduct == null || !Objects.equals(liveProduct.getLiveRoomId(), liveRoomId)) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在或不属于该直播间");
            }
        }
        
        liveRoom.setCurrentProductId(productId);
        liveRoom.setUpdaterId(1L);
        
        return this.updateById(liveRoom);
    }

    @Override
    public LiveStatsVO getLiveRoomStats(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间ID不能为空");
        }
        
        LiveStatsVO stats = new LiveStatsVO();
        
        Map<String, Object> orderSummary = liveOrderMapper.getOrderSalesSummary(liveRoomId);
        if (orderSummary != null) {
            stats.setTotalOrders(((Number) orderSummary.getOrDefault("totalOrders", 0)).intValue());
            stats.setTotalSales((BigDecimal) orderSummary.getOrDefault("totalSales", BigDecimal.ZERO));
            stats.setPaidOrders(((Number) orderSummary.getOrDefault("paidOrders", 0)).intValue());
            stats.setPaidSales((BigDecimal) orderSummary.getOrDefault("paidSales", BigDecimal.ZERO));
        }
        
        List<Map<String, Object>> productRanking = liveOrderMapper.getProductSalesRanking(liveRoomId);
        stats.setProductRanking(productRanking);
        
        List<Map<String, Object>> viewerTrend = liveDataMapper.getLiveViewerTrend(liveRoomId);
        stats.setViewerTrend(viewerTrend);
        
        BigDecimal avgRetention = liveDataMapper.getAverageRetentionRate(liveRoomId);
        stats.setAverageRetentionRate(avgRetention != null ? avgRetention : BigDecimal.ZERO);
        
        LiveRoom liveRoom = this.getById(liveRoomId);
        if (liveRoom != null && liveRoom.getStartTime() != null && liveRoom.getEndTime() != null) {
            List<Map<String, Object>> retentionCurve = liveDataMapper.getRetentionCurve(
                    liveRoomId, liveRoom.getStartTime(), liveRoom.getEndTime());
            stats.setRetentionCurve(retentionCurve);
        } else {
            List<Map<String, Object>> retentionCurve = liveDataMapper.getRetentionCurve(
                    liveRoomId, LocalDateTime.now().minusHours(1), LocalDateTime.now());
            stats.setRetentionCurve(retentionCurve);
        }
        
        LambdaQueryWrapper<LiveData> dataWrapper = new LambdaQueryWrapper<>();
        dataWrapper.eq(LiveData::getLiveRoomId, liveRoomId);
        dataWrapper.select(LiveData::getProductClickCount, LiveData::getInteractionCount);
        List<LiveData> dataList = liveDataMapper.selectList(dataWrapper);
        
        int totalClicks = 0;
        int totalInteractions = 0;
        for (LiveData data : dataList) {
            if (data.getProductClickCount() != null) {
                totalClicks += data.getProductClickCount();
            }
            if (data.getInteractionCount() != null) {
                totalInteractions += data.getInteractionCount();
            }
        }
        stats.setProductClickCount(totalClicks);
        stats.setInteractionCount(totalInteractions);
        
        if (totalClicks > 0) {
            BigDecimal conversionRate = liveOrderMapper.calculateConversionRate(
                    stats.getTotalOrders(), totalClicks);
            stats.setConversionRate(conversionRate);
        } else {
            stats.setConversionRate(BigDecimal.ZERO);
        }
        
        return stats;
    }
}