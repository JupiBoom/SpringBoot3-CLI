package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.liveroom.LiveRoomQueryRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.ILiveRoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    @Override
    public LiveRoomVO getLiveRoomVO(LiveRoom liveRoom) {
        return Optional.ofNullable(liveRoom)
                .map(l -> BeanUtil.copyProperties(l, LiveRoomVO.class))
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<LiveRoom> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), LiveRoom::getId);
        QueryWrapperUtil.addLikeCondition(queryWrapper, queryRequest.getTitle(), LiveRoom::getTitle);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getAnchorId(), LiveRoom::getAnchorId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), LiveRoom::getStatus);

        if (queryRequest.getStartTimeBegin() != null) {
            queryWrapper.ge(LiveRoom::getStartTime, queryRequest.getStartTimeBegin());
        }
        if (queryRequest.getStartTimeEnd() != null) {
            queryWrapper.le(LiveRoom::getStartTime, queryRequest.getStartTimeEnd());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                LiveRoom::getId);

        return queryWrapper;
    }

    @Override
    public boolean updateLiveRoomStatus(Long liveRoomId, Byte status) {
        if (liveRoomId == null || status == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LiveRoom liveRoom = this.getById(liveRoomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }

        LambdaUpdateWrapper<LiveRoom> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LiveRoom::getId, liveRoomId)
                .set(LiveRoom::getStatus, status);

        if (status == 1) {
            updateWrapper.set(LiveRoom::getStartTime, LocalDateTime.now());
        } else if (status == 2) {
            updateWrapper.set(LiveRoom::getEndTime, LocalDateTime.now());
        }

        return this.update(updateWrapper);
    }

    @Override
    public boolean startLive(Long liveRoomId) {
        return updateLiveRoomStatus(liveRoomId, (byte) 1);
    }

    @Override
    public boolean endLive(Long liveRoomId) {
        return updateLiveRoomStatus(liveRoomId, (byte) 2);
    }
}
