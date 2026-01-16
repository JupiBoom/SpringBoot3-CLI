package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveRoomViewer;
import com.rosy.main.domain.vo.LiveRoomViewerVO;
import com.rosy.main.mapper.LiveRoomViewerMapper;
import com.rosy.main.service.ILiveRoomViewerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveRoomViewerServiceImpl extends ServiceImpl<LiveRoomViewerMapper, LiveRoomViewer> implements ILiveRoomViewerService {

    @Override
    public LiveRoomViewerVO recordViewerEntry(LiveRoomViewer liveRoomViewer) {
        liveRoomViewer.setEnterTime(LocalDateTime.now());
        liveRoomViewer.setDuration(0);
        liveRoomViewer.setCreateTime(LocalDateTime.now());
        liveRoomViewer.setUpdateTime(LocalDateTime.now());
        boolean saved = save(liveRoomViewer);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "记录观众进入失败");
        }
        return getLiveRoomViewerVO(liveRoomViewer);
    }

    @Override
    public boolean recordViewerExit(Long id) {
        LiveRoomViewer viewer = getById(id);
        if (viewer == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "观众记录不存在");
        }
        if (viewer.getLeaveTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "观众已离开");
        }

        LocalDateTime leaveTime = LocalDateTime.now();
        long duration = ChronoUnit.SECONDS.between(viewer.getEnterTime(), leaveTime);
        viewer.setLeaveTime(leaveTime);
        viewer.setDuration((int) duration);
        viewer.setUpdateTime(LocalDateTime.now());

        return updateById(viewer);
    }

    @Override
    public List<LiveRoomViewerVO> getViewerList(Long liveRoomId) {
        List<LiveRoomViewer> viewers = baseMapper.getViewerList(liveRoomId);
        return viewers.stream().map(this::getLiveRoomViewerVO).collect(Collectors.toList());
    }

    @Override
    public Integer getTotalViewers(Long liveRoomId) {
        return baseMapper.getTotalViewers(liveRoomId);
    }

    @Override
    public Long getAvgViewDuration(Long liveRoomId) {
        return baseMapper.getAvgViewDuration(liveRoomId);
    }

    @Override
    public LiveRoomViewerVO getLiveRoomViewerVO(LiveRoomViewer liveRoomViewer) {
        if (liveRoomViewer == null) {
            return null;
        }
        return BeanUtil.copyProperties(liveRoomViewer, LiveRoomViewerVO.class);
    }
}