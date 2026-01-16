package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.ILiveRoomProductService;
import com.rosy.main.service.ILiveRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    @Autowired
    private ILiveRoomProductService liveRoomProductService;

    @Override
    public LiveRoomVO createLiveRoom(LiveRoom liveRoom) {
        liveRoom.setStatus((byte) 0);
        liveRoom.setCreateTime(LocalDateTime.now());
        liveRoom.setUpdateTime(LocalDateTime.now());
        boolean saved = save(liveRoom);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建直播间失败");
        }
        return getLiveRoomVO(liveRoom);
    }

    @Override
    public LiveRoomVO updateLiveRoom(LiveRoom liveRoom) {
        LiveRoom existLiveRoom = getById(liveRoom.getId());
        if (existLiveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        liveRoom.setUpdateTime(LocalDateTime.now());
        boolean updated = updateById(liveRoom);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新直播间失败");
        }
        return getLiveRoomVO(liveRoom);
    }

    @Override
    public boolean deleteLiveRoom(Long id) {
        LiveRoom liveRoom = getById(id);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (liveRoom.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播中的直播间不能删除");
        }
        return removeById(id);
    }

    @Override
    public LiveRoomVO getLiveRoomDetail(Long id) {
        LiveRoom liveRoom = getById(id);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        return getLiveRoomVO(liveRoom);
    }

    @Override
    public List<LiveRoomVO> getLiveRoomList(Long anchorId, Byte status) {
        LambdaQueryWrapper<LiveRoom> queryWrapper = new LambdaQueryWrapper<>();
        if (anchorId != null) {
            queryWrapper.eq(LiveRoom::getAnchorId, anchorId);
        }
        if (status != null) {
            queryWrapper.eq(LiveRoom::getStatus, status);
        }
        queryWrapper.orderByDesc(LiveRoom::getCreateTime);
        List<LiveRoom> liveRooms = list(queryWrapper);
        return liveRooms.stream().map(this::getLiveRoomVO).collect(Collectors.toList());
    }

    @Override
    public boolean startLive(Long id) {
        LiveRoom liveRoom = getById(id);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (liveRoom.getStatus() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播间状态不正确");
        }
        liveRoom.setStatus((byte) 1);
        liveRoom.setStartTime(LocalDateTime.now());
        liveRoom.setUpdateTime(LocalDateTime.now());
        return updateById(liveRoom);
    }

    @Override
    public boolean endLive(Long id) {
        LiveRoom liveRoom = getById(id);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }
        if (liveRoom.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播间状态不正确");
        }
        liveRoom.setStatus((byte) 2);
        liveRoom.setEndTime(LocalDateTime.now());
        liveRoom.setUpdateTime(LocalDateTime.now());
        return updateById(liveRoom);
    }

    @Override
    public LiveRoomVO getLiveRoomVO(LiveRoom liveRoom) {
        if (liveRoom == null) {
            return null;
        }
        LiveRoomVO liveRoomVO = BeanUtil.copyProperties(liveRoom, LiveRoomVO.class);
        liveRoomVO.setStatusText(getStatusText(liveRoom.getStatus()));
        return liveRoomVO;
    }

    private String getStatusText(Byte status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 0:
                return "未开始";
            case 1:
                return "直播中";
            case 2:
                return "已结束";
            default:
                return "未知";
        }
    }
}