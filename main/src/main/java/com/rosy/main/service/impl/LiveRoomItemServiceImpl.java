package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveRoomItem;
import com.rosy.main.domain.vo.LiveRoomItemVO;
import com.rosy.main.mapper.LiveRoomItemMapper;
import com.rosy.main.service.IItemService;
import com.rosy.main.service.ILiveRoomItemService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveRoomItemServiceImpl extends ServiceImpl<LiveRoomItemMapper, LiveRoomItem> implements ILiveRoomItemService {

    @Resource
    private IItemService itemService;

    @Override
    public LiveRoomItemVO getLiveRoomItemVO(LiveRoomItem liveRoomItem) {
        LiveRoomItemVO liveRoomItemVO = new LiveRoomItemVO();
        BeanUtil.copyProperties(liveRoomItem, liveRoomItemVO);
        liveRoomItemVO.setItem(itemService.getItemVO(itemService.getById(liveRoomItem.getItemId())));
        return liveRoomItemVO;
    }

    @Override
    public List<LiveRoomItemVO> getLiveRoomItems(Long liveRoomId) {
        LambdaQueryWrapper<LiveRoomItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomItem::getLiveRoomId, liveRoomId);
        wrapper.eq(LiveRoomItem::getStatus, 1);
        wrapper.orderByAsc(LiveRoomItem::getSortOrder);
        return this.list(wrapper)
                .stream()
                .map(this::getLiveRoomItemVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addLiveRoomItem(Long liveRoomId, Long itemId, String sellingPoints, Integer sortOrder) {
        LambdaQueryWrapper<LiveRoomItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomItem::getLiveRoomId, liveRoomId);
        wrapper.eq(LiveRoomItem::getItemId, itemId);
        LiveRoomItem existing = this.getOne(wrapper);
        if (existing != null) {
            existing.setStatus((byte) 1);
            existing.setSellingPoints(sellingPoints);
            existing.setSortOrder(sortOrder);
            return this.updateById(existing);
        }
        LiveRoomItem liveRoomItem = new LiveRoomItem();
        liveRoomItem.setLiveRoomId(liveRoomId);
        liveRoomItem.setItemId(itemId);
        liveRoomItem.setSellingPoints(sellingPoints);
        liveRoomItem.setSortOrder(sortOrder);
        liveRoomItem.setStatus((byte) 1);
        return this.save(liveRoomItem);
    }

    @Override
    public boolean removeLiveRoomItem(Long liveRoomId, Long itemId) {
        LambdaQueryWrapper<LiveRoomItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomItem::getLiveRoomId, liveRoomId);
        wrapper.eq(LiveRoomItem::getItemId, itemId);
        return this.remove(wrapper);
    }
}