package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveRoomItemRank;
import com.rosy.main.domain.entity.LiveRoomSales;
import com.rosy.main.mapper.LiveRoomItemRankMapper;
import com.rosy.main.service.ILiveRoomItemRankService;
import com.rosy.main.service.ILiveRoomSalesService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveRoomItemRankServiceImpl extends ServiceImpl<LiveRoomItemRankMapper, LiveRoomItemRank> implements ILiveRoomItemRankService {

    @Resource
    private ILiveRoomSalesService liveRoomSalesService;

    @Override
    public List<LiveRoomItemRank> getLiveRoomItemRank(Long liveRoomId) {
        LambdaQueryWrapper<LiveRoomItemRank> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomItemRank::getLiveRoomId, liveRoomId);
        wrapper.orderByAsc(LiveRoomItemRank::getRank);
        return this.list(wrapper);
    }

    @Override
    public boolean updateItemRank(Long liveRoomId) {
        // 获取直播间所有商品销售数据
        List<LiveRoomSales> salesList = liveRoomSalesService.getLiveRoomSales(liveRoomId);
        if (salesList.isEmpty()) {
            return false;
        }

        // 按销售额降序排序
        List<LiveRoomSales> sortedSalesList = salesList.stream()
                .sorted(Comparator.comparing(LiveRoomSales::getSalesAmount).reversed())
                .collect(Collectors.toList());

        // 更新排行榜
        for (int i = 0; i < sortedSalesList.size(); i++) {
            LiveRoomSales sales = sortedSalesList.get(i);
            LambdaQueryWrapper<LiveRoomItemRank> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LiveRoomItemRank::getLiveRoomId, liveRoomId);
            wrapper.eq(LiveRoomItemRank::getItemId, sales.getItemId());
            LiveRoomItemRank rank = this.getOne(wrapper);

            if (rank == null) {
                rank = new LiveRoomItemRank();
                rank.setLiveRoomId(liveRoomId);
                rank.setItemId(sales.getItemId());
                rank.setSalesAmount(sales.getSalesAmount());
                rank.setOrderCount(sales.getOrderCount());
                rank.setRank(i + 1);
                this.save(rank);
            } else {
                rank.setSalesAmount(sales.getSalesAmount());
                rank.setOrderCount(sales.getOrderCount());
                rank.setRank(i + 1);
                this.updateById(rank);
            }
        }
        return true;
    }
}
