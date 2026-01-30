package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveRoomExplanation;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.IItemService;
import com.rosy.main.service.ILiveRoomExplanationService;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    @Resource
    private IItemService itemService;

    @Resource
    private ILiveRoomExplanationService liveRoomExplanationService;

    @Override
    public LiveRoomVO getLiveRoomVO(LiveRoom liveRoom) {
        LiveRoomVO liveRoomVO = new LiveRoomVO();
        BeanUtil.copyProperties(liveRoom, liveRoomVO);
        if (liveRoom.getCurrentItemId() != null) {
            liveRoomVO.setCurrentItem(itemService.getItemVO(itemService.getById(liveRoom.getCurrentItemId())));
        }
        return liveRoomVO;
    }

    @Override
    public LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest liveRoomQueryRequest) {
        LambdaQueryWrapper<LiveRoom> queryWrapper = new LambdaQueryWrapper<>();
        if (liveRoomQueryRequest == null) {
            return queryWrapper;
        }
        queryWrapper.eq(liveRoomQueryRequest.getId() != null, LiveRoom::getId, liveRoomQueryRequest.getId());
        queryWrapper.like(liveRoomQueryRequest.getName() != null, LiveRoom::getName, liveRoomQueryRequest.getName());
        queryWrapper.eq(liveRoomQueryRequest.getStatus() != null, LiveRoom::getStatus, liveRoomQueryRequest.getStatus());
        queryWrapper.eq(liveRoomQueryRequest.getCreatorId() != null, LiveRoom::getCreatorId, liveRoomQueryRequest.getCreatorId());
        queryWrapper.orderByDesc(LiveRoom::getCreateTime);
        return queryWrapper;
    }

    @Override
    public boolean startLive(Long id) {
        LiveRoom liveRoom = this.getById(id);
        if (liveRoom == null || liveRoom.getStatus() == 1) {
            return false;
        }
        liveRoom.setStatus((byte) 1);
        liveRoom.setStartTime(LocalDateTime.now());
        return this.updateById(liveRoom);
    }

    @Override
    public boolean endLive(Long id) {
        LiveRoom liveRoom = this.getById(id);
        if (liveRoom == null || liveRoom.getStatus() != 1) {
            return false;
        }
        liveRoom.setStatus((byte) 2);
        liveRoom.setEndTime(LocalDateTime.now());
        return this.updateById(liveRoom);
    }

    @Override
    public boolean switchItem(Long liveRoomId, Long itemId) {
        LiveRoom liveRoom = this.getById(liveRoomId);
        if (liveRoom == null || liveRoom.getStatus() != 1) {
            return false;
        }
        // 结束当前商品讲解
        LambdaQueryWrapper<LiveRoomExplanation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveRoomExplanation::getLiveRoomId, liveRoomId);
        wrapper.isNull(LiveRoomExplanation::getEndTime);
        LiveRoomExplanation currentExplanation = liveRoomExplanationService.getOne(wrapper);
        if (currentExplanation != null) {
            currentExplanation.setEndTime(LocalDateTime.now());
            liveRoomExplanationService.updateById(currentExplanation);
        }
        // 开始新商品讲解
        LiveRoomExplanation newExplanation = new LiveRoomExplanation();
        newExplanation.setLiveRoomId(liveRoomId);
        newExplanation.setItemId(itemId);
        newExplanation.setStartTime(LocalDateTime.now());
        liveRoomExplanationService.save(newExplanation);
        // 更新直播间当前商品
        liveRoom.setCurrentItemId(itemId);
        return this.updateById(liveRoom);
    }
}