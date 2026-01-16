package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveRoomData;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.entity.LiveRoomViewer;
import com.rosy.main.domain.vo.*;
import com.rosy.main.mapper.LiveRoomDataMapper;
import com.rosy.main.mapper.LiveRoomProductMapper;
import com.rosy.main.service.ILiveRoomDataService;
import com.rosy.main.service.ILiveRoomService;
import com.rosy.main.service.ILiveRoomViewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveRoomDataServiceImpl extends ServiceImpl<LiveRoomDataMapper, LiveRoomData> implements ILiveRoomDataService {

    @Autowired
    private ILiveRoomService liveRoomService;

    @Autowired
    private ILiveRoomViewerService liveRoomViewerService;

    @Autowired
    private LiveRoomProductMapper liveRoomProductMapper;

    @Override
    public LiveRoomDataVO recordLiveRoomData(LiveRoomData liveRoomData) {
        liveRoomData.setRecordTime(LocalDateTime.now());
        liveRoomData.setCreateTime(LocalDateTime.now());
        liveRoomData.setUpdateTime(LocalDateTime.now());
        boolean saved = save(liveRoomData);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "记录数据失败");
        }
        return getLiveRoomDataVO(liveRoomData);
    }

    @Override
    public List<LiveRoomDataVO> getLiveRoomDataList(Long liveRoomId) {
        LambdaQueryWrapper<LiveRoomData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomData::getLiveRoomId, liveRoomId);
        queryWrapper.orderByDesc(LiveRoomData::getRecordTime);
        List<LiveRoomData> dataList = list(queryWrapper);
        return dataList.stream().map(this::getLiveRoomDataVO).collect(Collectors.toList());
    }

    @Override
    public LiveRoomDataVO getLatestData(Long liveRoomId) {
        LiveRoomData data = baseMapper.getLatestData(liveRoomId);
        return getLiveRoomDataVO(data);
    }

    @Override
    public LiveRoomAnalyticsVO getAnalytics(Long liveRoomId) {
        LiveRoom liveRoom = liveRoomService.getById(liveRoomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }

        LiveRoomAnalyticsVO analytics = new LiveRoomAnalyticsVO();
        analytics.setLiveRoomId(liveRoomId);
        analytics.setLiveRoomTitle(liveRoom.getTitle());
        analytics.setStartTime(liveRoom.getStartTime());
        analytics.setEndTime(liveRoom.getEndTime());

        LiveRoomData totalData = baseMapper.getTotalData(liveRoomId);
        if (totalData != null) {
            analytics.setTotalOrders(totalData.getTotalOrders());
            analytics.setTotalSales(totalData.getTotalSales());
        }

        Integer totalViewers = liveRoomViewerService.getTotalViewers(liveRoomId);
        analytics.setTotalViewers(totalViewers != null ? totalViewers : 0);

        Long avgDuration = liveRoomViewerService.getAvgViewDuration(liveRoomId);
        analytics.setAvgViewDuration(avgDuration != null ? BigDecimal.valueOf(avgDuration) : BigDecimal.ZERO);

        if (totalViewers != null && totalViewers > 0 && totalData != null && totalData.getTotalOrders() != null) {
            BigDecimal conversionRate = BigDecimal.valueOf(totalData.getTotalOrders())
                    .divide(BigDecimal.valueOf(totalViewers), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            analytics.setConversionRate(conversionRate);
        } else {
            analytics.setConversionRate(BigDecimal.ZERO);
        }

        ViewerRetentionVO viewerRetention = getViewerRetention(liveRoomId);
        analytics.setViewerRetention(viewerRetention);

        return analytics;
    }

    @Override
    public List<ProductRankingVO> getProductRanking(Long liveRoomId) {
        List<LiveRoomProduct> products = liveRoomProductMapper.getProductRanking(liveRoomId);
        return products.stream().map(product -> {
            ProductRankingVO vo = new ProductRankingVO();
            vo.setProductId(product.getProductId());
            vo.setProductName(product.getProductName());
            vo.setProductImage(product.getProductImage());
            vo.setProductPrice(product.getProductPrice());
            vo.setSellingPoint(product.getSellingPoint());
            vo.setOrderCount(product.getOrderCount());
            vo.setSalesAmount(product.getSalesAmount());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public ViewerRetentionVO getViewerRetention(Long liveRoomId) {
        List<LiveRoomData> dataList = baseMapper.getViewerTrend(liveRoomId);
        ViewerRetentionVO viewerRetention = new ViewerRetentionVO();
        List<ViewerRetentionVO.RetentionPointVO> retentionPoints = new ArrayList<>();

        if (dataList.isEmpty()) {
            viewerRetention.setRetentionPoints(retentionPoints);
            return viewerRetention;
        }

        int initialViewers = dataList.get(0).getViewerCount();
        LocalDateTime startTime = dataList.get(0).getRecordTime();

        for (LiveRoomData data : dataList) {
            ViewerRetentionVO.RetentionPointVO point = new ViewerRetentionVO.RetentionPointVO();
            long minutes = ChronoUnit.MINUTES.between(startTime, data.getRecordTime());
            point.setMinute((int) minutes);
            point.setViewerCount(data.getViewerCount());

            if (initialViewers > 0) {
                double retentionRate = (double) data.getViewerCount() / initialViewers * 100;
                point.setRetentionRate(retentionRate);
            } else {
                point.setRetentionRate(0.0);
            }

            retentionPoints.add(point);
        }

        viewerRetention.setRetentionPoints(retentionPoints);
        return viewerRetention;
    }

    private LiveRoomDataVO getLiveRoomDataVO(LiveRoomData liveRoomData) {
        if (liveRoomData == null) {
            return null;
        }
        return BeanUtil.copyProperties(liveRoomData, LiveRoomDataVO.class);
    }
}