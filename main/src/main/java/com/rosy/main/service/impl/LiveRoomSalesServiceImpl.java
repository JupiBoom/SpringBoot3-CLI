package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveRoomSales;
import com.rosy.main.mapper.LiveRoomSalesMapper;
import com.rosy.main.service.ILiveRoomSalesService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LiveRoomSalesServiceImpl extends ServiceImpl<LiveRoomSalesMapper, LiveRoomSales> implements ILiveRoomSalesService {

    @Override
    public List<LiveRoomSales> getLiveRoomSales(Long liveRoomId) {
        LambdaQueryWrapper<LiveRoomSales> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomSales::getLiveRoomId, liveRoomId);
        return this.list(wrapper);
    }

    @Override
    public boolean addSalesData(Long liveRoomId, Long itemId, Integer orderCount, Double salesAmount) {
        LambdaQueryWrapper<LiveRoomSales> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomSales::getLiveRoomId, liveRoomId);
        wrapper.eq(LiveRoomSales::getItemId, itemId);
        LiveRoomSales liveRoomSales = this.getOne(wrapper);
        if (liveRoomSales == null) {
            liveRoomSales = new LiveRoomSales();
            liveRoomSales.setLiveRoomId(liveRoomId);
            liveRoomSales.setItemId(itemId);
            liveRoomSales.setOrderCount(orderCount);
            liveRoomSales.setSalesAmount(BigDecimal.valueOf(salesAmount));
            return this.save(liveRoomSales);
        } else {
            liveRoomSales.setOrderCount(liveRoomSales.getOrderCount() + orderCount);
            liveRoomSales.setSalesAmount(liveRoomSales.getSalesAmount().add(BigDecimal.valueOf(salesAmount)));
            return this.updateById(liveRoomSales);
        }
    }
}