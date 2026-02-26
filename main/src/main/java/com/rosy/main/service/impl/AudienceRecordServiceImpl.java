package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.AudienceRecord;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.mapper.AudienceRecordMapper;
import com.rosy.main.service.IAudienceRecordService;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AudienceRecordServiceImpl extends ServiceImpl<AudienceRecordMapper, AudienceRecord> implements IAudienceRecordService {

    @Resource
    private ILiveRoomService liveRoomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordAudience(Long liveRoomId, Integer viewerCount, Integer newViewerCount, Integer leaveViewerCount) {
        if (liveRoomId == null || viewerCount == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        AudienceRecord record = new AudienceRecord();
        record.setLiveRoomId(liveRoomId);
        record.setViewerCount(viewerCount);
        record.setNewViewerCount(newViewerCount != null ? newViewerCount : 0);
        record.setLeaveViewerCount(leaveViewerCount != null ? leaveViewerCount : 0);
        record.setRecordTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());

        boolean saved = this.save(record);
        if (saved) {
            updateLiveRoomViewerCount(liveRoomId, viewerCount, newViewerCount);
        }

        return saved;
    }

    private void updateLiveRoomViewerCount(Long liveRoomId, Integer viewerCount, Integer newViewerCount) {
        LiveRoom liveRoom = liveRoomService.getById(liveRoomId);
        if (liveRoom != null) {
            liveRoom.setViewerCount(viewerCount);
            if (newViewerCount != null && newViewerCount > 0) {
                liveRoom.setTotalViewerCount(liveRoom.getTotalViewerCount() + newViewerCount);
            }
            liveRoomService.updateById(liveRoom);
        }
    }

    @Override
    public List<AudienceRecord> getAudienceRecords(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<AudienceRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AudienceRecord::getLiveRoomId, liveRoomId)
                .orderByAsc(AudienceRecord::getRecordTime);

        return this.list(queryWrapper);
    }

    @Override
    public Integer getPeakViewerCount(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<AudienceRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AudienceRecord::getLiveRoomId, liveRoomId)
                .orderByDesc(AudienceRecord::getViewerCount)
                .last("LIMIT 1");

        AudienceRecord record = this.getOne(queryWrapper);
        return record != null ? record.getViewerCount() : 0;
    }
}
