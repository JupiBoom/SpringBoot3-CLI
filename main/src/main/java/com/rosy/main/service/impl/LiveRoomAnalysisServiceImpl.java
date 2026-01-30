package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.main.domain.entity.LiveRoomAudience;
import com.rosy.main.domain.vo.LiveRoomConversionRateVO;
import com.rosy.main.domain.vo.LiveRoomRetentionVO;
import com.rosy.main.mapper.LiveRoomAudienceMapper;
import com.rosy.main.service.ILiveRoomAnalysisService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LiveRoomAnalysisServiceImpl implements ILiveRoomAnalysisService {

    @Resource
    private LiveRoomAudienceMapper liveRoomAudienceMapper;

    @Override
    public LiveRoomConversionRateVO calculateConversionRate(Long liveRoomId) {
        // 查询总观众数
        LambdaQueryWrapper<LiveRoomAudience> totalWrapper = new LambdaQueryWrapper<>();
        totalWrapper.eq(LiveRoomAudience::getLiveRoomId, liveRoomId);
        int totalAudience = liveRoomAudienceMapper.selectCount(totalWrapper).intValue();

        // 查询购买人数
        LambdaQueryWrapper<LiveRoomAudience> purchaseWrapper = new LambdaQueryWrapper<>();
        purchaseWrapper.eq(LiveRoomAudience::getLiveRoomId, liveRoomId);
        purchaseWrapper.eq(LiveRoomAudience::getIsPurchase, 1);
        int totalPurchases = liveRoomAudienceMapper.selectCount(purchaseWrapper).intValue();

        // 计算转化率
        BigDecimal conversionRate = BigDecimal.ZERO;
        if (totalAudience > 0) {
            conversionRate = BigDecimal.valueOf(totalPurchases)
                    .divide(BigDecimal.valueOf(totalAudience), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        LiveRoomConversionRateVO vo = new LiveRoomConversionRateVO();
        vo.setLiveRoomId(liveRoomId);
        vo.setTotalAudience(totalAudience);
        vo.setTotalPurchases(totalPurchases);
        vo.setConversionRate(conversionRate);
        vo.setFormattedConversionRate(conversionRate.setScale(2, RoundingMode.HALF_UP) + "%");

        return vo;
    }

    @Override
    public LiveRoomRetentionVO getAudienceRetentionCurve(Long liveRoomId) {
        // 查询所有观众数据
        LambdaQueryWrapper<LiveRoomAudience> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomAudience::getLiveRoomId, liveRoomId);
        wrapper.orderByAsc(LiveRoomAudience::getEnterTime);
        List<LiveRoomAudience> audienceList = liveRoomAudienceMapper.selectList(wrapper);

        // 计算留存曲线数据点（每5分钟一个点）
        List<LiveRoomRetentionVO.RetentionPoint> retentionPoints = new ArrayList<>();
        if (audienceList.isEmpty()) {
            return new LiveRoomRetentionVO();
        }

        LocalDateTime startTime = audienceList.get(0).getEnterTime();
        LocalDateTime endTime = LocalDateTime.now();

        // 按时间间隔计算留存
        for (LocalDateTime timePoint = startTime; timePoint.isBefore(endTime.plusMinutes(5)); timePoint = timePoint.plusMinutes(5)) {
            LiveRoomRetentionVO.RetentionPoint point = new LiveRoomRetentionVO.RetentionPoint();
            point.setTimePoint(timePoint);

            // 计算该时间点前进入的观众数
            int totalAudienceUpToTime = 0;
            int retainedAudienceAtTime = 0;

            for (LiveRoomAudience audience : audienceList) {
                if (audience.getEnterTime().isBefore(timePoint)) {
                    totalAudienceUpToTime++;
                    // 观众在该时间点仍在直播间（没有离开时间或离开时间在该时间点之后）
                    if (audience.getLeaveTime() == null || audience.getLeaveTime().isAfter(timePoint)) {
                        retainedAudienceAtTime++;
                    }
                }
            }

            point.setAudienceCount(totalAudienceUpToTime);
            point.setRetainedCount(retainedAudienceAtTime);

            double retentionRate = 0.0;
            if (totalAudienceUpToTime > 0) {
                retentionRate = (double) retainedAudienceAtTime / totalAudienceUpToTime * 100;
            }
            point.setRetentionRate(retentionRate);

            retentionPoints.add(point);
        }

        LiveRoomRetentionVO vo = new LiveRoomRetentionVO();
        vo.setLiveRoomId(liveRoomId);
        vo.setRetentionPoints(retentionPoints);

        return vo;
    }
}