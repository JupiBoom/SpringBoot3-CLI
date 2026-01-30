package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomSales;

import java.util.List;

public interface ILiveRoomSalesService extends IService<LiveRoomSales> {

    List<LiveRoomSales> getLiveRoomSales(Long liveRoomId);

    boolean addSalesData(Long liveRoomId, Long itemId, Integer orderCount, Double salesAmount);
}