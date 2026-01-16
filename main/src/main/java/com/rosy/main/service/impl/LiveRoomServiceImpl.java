package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveData;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveRoomCreateVO;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.ILiveDataService;
import com.rosy.main.service.ILiveRoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 直播间表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    private final ILiveDataService liveDataService;

    public LiveRoomServiceImpl(ILiveDataService liveDataService) {
        this.liveDataService = liveDataService;
    }

    @Override
    @Transactional
    public LiveRoomVO createLiveRoom(LiveRoomCreateVO liveRoomCreateVO) {
        LiveRoom liveRoom = BeanUtil.copyProperties(liveRoomCreateVO, LiveRoom.class);
        liveRoom.setStatus((byte) 0);
        liveRoom.setCreateTime(LocalDateTime.now());
        liveRoom.setUpdateTime(LocalDateTime.now());
        this.save(liveRoom);
        
        // 初始化数据记录
        LiveData liveData = new LiveData();
        liveData.setLiveRoomId(liveRoom.getId());
        liveDataService.save(liveData);
        
        return this.toVO(liveRoom);
    }

    @Override
    @Transactional
    public LiveRoomVO startLive(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间不存在");
        }
        if (liveRoom.getStatus() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播已在进行中");
        }
        liveRoom.setStatus((byte) 1);
        liveRoom.setStartTime(LocalDateTime.now());
        liveRoom.setUpdateTime(LocalDateTime.now());
        this.updateById(liveRoom);
        return this.toVO(liveRoom);
    }

    @Override
    @Transactional
    public LiveRoomVO endLive(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间不存在");
        }
        if (liveRoom.getStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "直播未在进行中");
        }
        liveRoom.setStatus((byte) 2);
        liveRoom.setEndTime(LocalDateTime.now());
        liveRoom.setUpdateTime(LocalDateTime.now());
        this.updateById(liveRoom);
        return this.toVO(liveRoom);
    }

    @Override
    @Transactional
    public LiveRoomVO switchCurrentGoods(Long roomId, Long goodsId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间不存在");
        }
        liveRoom.setCurrentGoodsId(goodsId);
        liveRoom.setUpdateTime(LocalDateTime.now());
        this.updateById(liveRoom);
        return this.toVO(liveRoom);
    }

    @Override
    public LiveRoomVO getLiveRoomDetail(Long roomId) {
        LiveRoom liveRoom = this.getById(roomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "直播间不存在");
        }
        return this.toVO(liveRoom);
    }

    @Override
    public LiveRoomVO toVO(LiveRoom liveRoom) {
        return BeanUtil.copyProperties(liveRoom, LiveRoomVO.class);
    }
}
