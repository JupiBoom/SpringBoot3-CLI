package com.rosy.main.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.LiveRoomItemAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomItemUpdateRequest;
import com.rosy.main.domain.entity.LiveRoomItem;
import com.rosy.main.domain.vo.LiveRoomItemVO;
import com.rosy.main.mapper.LiveRoomItemMapper;
import com.rosy.main.service.ILiveRoomItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间商品关联表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@Service
public class LiveRoomItemServiceImpl extends ServiceImpl<LiveRoomItemMapper, LiveRoomItem> implements ILiveRoomItemService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addLiveRoomItem(LiveRoomItemAddRequest request, Long creatorId) {
        // 检查是否已存在
        LambdaQueryWrapper<LiveRoomItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomItem::getRoomId, request.getRoomId())
                .eq(LiveRoomItem::getItemId, request.getItemId())
                .eq(LiveRoomItem::getIsDeleted, 0);
        LiveRoomItem existItem = this.getOne(queryWrapper);
        ThrowUtils.throwIf(existItem != null, ErrorCode.OPERATION_ERROR, "该商品已在直播间中");

        LiveRoomItem liveRoomItem = new LiveRoomItem();
        BeanUtils.copyProperties(request, liveRoomItem);
        
        // 处理卖点列表
        if (request.getSellingPoints() != null && !request.getSellingPoints().isEmpty()) {
            liveRoomItem.setSellingPoints(JSONUtil.toJsonStr(request.getSellingPoints()));
        }
        
        liveRoomItem.setStatus((byte) 1);
        liveRoomItem.setSalesCount(0);
        liveRoomItem.setSalesAmount(BigDecimal.ZERO);
        liveRoomItem.setCreatorId(creatorId);
        
        boolean result = this.save(liveRoomItem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return liveRoomItem.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLiveRoomItem(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLiveRoomItem(LiveRoomItemUpdateRequest request, Long updaterId) {
        LiveRoomItem liveRoomItem = new LiveRoomItem();
        BeanUtils.copyProperties(request, liveRoomItem);
        
        // 处理卖点列表
        if (request.getSellingPoints() != null) {
            liveRoomItem.setSellingPoints(JSONUtil.toJsonStr(request.getSellingPoints()));
        }
        
        liveRoomItem.setUpdaterId(updaterId);
        return this.updateById(liveRoomItem);
    }

    @Override
    public LiveRoomItemVO getLiveRoomItemById(Long id) {
        LiveRoomItem liveRoomItem = this.getById(id);
        if (liveRoomItem == null) {
            return null;
        }
        return convertToVO(liveRoomItem);
    }

    @Override
    public List<LiveRoomItemVO> listItemsByRoomId(Long roomId) {
        List<LiveRoomItem> list = baseMapper.selectByRoomId(roomId);
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<LiveRoomItemVO> getTopSellingItems(Long roomId, Integer limit) {
        List<LiveRoomItem> list = baseMapper.selectTopSellingItems(roomId, limit);
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateItemSalesData(Long roomId, Long itemId, Integer count, BigDecimal amount, Long updaterId) {
        return baseMapper.updateSalesData(roomId, itemId, count, amount, updaterId) > 0;
    }

    @Override
    public LiveRoomItemVO getCurrentItem(Long roomId) {
        LambdaQueryWrapper<LiveRoomItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveRoomItem::getRoomId, roomId)
                .eq(LiveRoomItem::getStatus, 1)
                .orderByAsc(LiveRoomItem::getSortOrder)
                .last("LIMIT 1");
        LiveRoomItem item = this.getOne(queryWrapper);
        return item != null ? convertToVO(item) : null;
    }

    private LiveRoomItemVO convertToVO(LiveRoomItem liveRoomItem) {
        LiveRoomItemVO vo = new LiveRoomItemVO();
        BeanUtils.copyProperties(liveRoomItem, vo);
        
        // 解析卖点JSON
        if (liveRoomItem.getSellingPoints() != null) {
            try {
                List<String> points = JSONUtil.toList(liveRoomItem.getSellingPoints(), String.class);
                vo.setSellingPoints(points);
            } catch (Exception e) {
                vo.setSellingPoints(null);
            }
        }
        
        // 设置状态文本
        if (liveRoomItem.getStatus() != null) {
            vo.setStatusText(liveRoomItem.getStatus() == 1 ? "上架" : "下架");
        }
        
        return vo;
    }
}
