package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveRoomData;
import com.rosy.main.mapper.LiveRoomDataMapper;
import com.rosy.main.service.ILiveRoomDataService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LiveRoomDataServiceImpl extends ServiceImpl<LiveRoomDataMapper, LiveRoomData> implements ILiveRoomDataService {

    @Override
    public LiveRoomData getLiveRoomData(Long liveRoomId) {
        LambdaQueryWrapper<LiveRoomData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomData::getLiveRoomId, liveRoomId);
        LiveRoomData liveRoomData = this.getOne(wrapper);
        if (liveRoomData == null) {
            liveRoomData = new LiveRoomData();
            liveRoomData.setLiveRoomId(liveRoomId);
            liveRoomData.setAudienceCount(0);
            liveRoomData.setPeakAudience(0);
            liveRoomData.setTotalOrders(0);
            liveRoomData.setTotalSales(BigDecimal.ZERO);
            this.save(liveRoomData);
        }
        return liveRoomData;
    }

    @Override
    public boolean updateAudienceCount(Long liveRoomId, Integer audienceCount) {
        LiveRoomData liveRoomData = this.getLiveRoomData(liveRoomId);
        liveRoomData.setAudienceCount(audienceCount);
        if (audienceCount > liveRoomData.getPeakAudience()) {
            liveRoomData.setPeakAudience(audienceCount);
        }
        return this.updateById(liveRoomData);
    }

    @Override
    public boolean updateSalesData(Long liveRoomId, Integer orderCount, Double salesAmount) {
        LiveRoomData liveRoomData = this.getLiveRoomData(liveRoomId);
        liveRoomData.setTotalOrders(liveRoomData.getTotalOrders() + orderCount);
        liveRoomData.setTotalSales(liveRoomData.getTotalSales().add(BigDecimal.valueOf(salesAmount)));
        return this.updateById(liveRoomData);
    }
}