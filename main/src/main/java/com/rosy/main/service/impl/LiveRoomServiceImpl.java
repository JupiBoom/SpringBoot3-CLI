package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.liveRoom.LiveRoomAddRequest;
import com.rosy.main.domain.dto.liveRoom.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.liveRoom.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.service.ILiveRoomService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 直播间表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Service
public class LiveRoomServiceImpl extends ServiceImpl<LiveRoomMapper, LiveRoom> implements ILiveRoomService {

    @Override
    public Long createLiveRoom(LiveRoomAddRequest liveRoomAddRequest) {
        LiveRoom liveRoom = new LiveRoom();
        BeanUtil.copyProperties(liveRoomAddRequest, liveRoom);
        liveRoom.setStatus((byte) 0); // 默认未开始
        liveRoom.setViewerCount(0); // 默认观众数为0
        boolean result = this.save(liveRoom);
        ThrowUtils.throwIf(!result, com.rosy.common.enums.ErrorCode.OPERATION_ERROR);
        return liveRoom.getId();
    }

    @Override
    public boolean updateLiveRoom(LiveRoomUpdateRequest liveRoomUpdateRequest) {
        LiveRoom liveRoom = new LiveRoom();
        BeanUtil.copyProperties(liveRoomUpdateRequest, liveRoom);
        return this.updateById(liveRoom);
    }

    @Override
    public LiveRoomVO getLiveRoomVO(LiveRoom liveRoom) {
        if (liveRoom == null) {
            return null;
        }
        LiveRoomVO liveRoomVO = new LiveRoomVO();
        BeanUtil.copyProperties(liveRoom, liveRoomVO);
        
        // 设置状态描述
        switch (liveRoom.getStatus()) {
            case 0:
                liveRoomVO.setStatusText("未开始");
                break;
            case 1:
                liveRoomVO.setStatusText("直播中");
                break;
            case 2:
                liveRoomVO.setStatusText("已结束");
                break;
            default:
                liveRoomVO.setStatusText("未知状态");
                break;
        }
        
        // TODO: 可以在这里添加其他关联数据的查询，如主播名称、当前商品名称等
        
        return liveRoomVO;
    }

    @Override
    public Page<LiveRoomVO> listLiveRoomVOByPage(LiveRoomQueryRequest liveRoomQueryRequest) {
        long current = liveRoomQueryRequest.getCurrent();
        long size = liveRoomQueryRequest.getPageSize();
        Page<LiveRoom> liveRoomPage = this.page(new Page<>(current, size), 
                this.getQueryWrapper(liveRoomQueryRequest));
        
        Page<LiveRoomVO> liveRoomVOPage = new Page<>(current, size, liveRoomPage.getTotal());
        liveRoomVOPage.setRecords(liveRoomPage.getRecords().stream()
                .map(this::getLiveRoomVO)
                .toList());
        
        return liveRoomVOPage;
    }

    @Override
    public boolean startLive(Long liveRoomId) {
        UpdateWrapper<LiveRoom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", liveRoomId)
                .set("status", 1)
                .set("start_time", LocalDateTime.now());
        return this.update(updateWrapper);
    }

    @Override
    public boolean endLive(Long liveRoomId) {
        UpdateWrapper<LiveRoom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", liveRoomId)
                .set("status", 2)
                .set("end_time", LocalDateTime.now());
        return this.update(updateWrapper);
    }

    @Override
    public boolean updateViewerCount(Long liveRoomId, Integer viewerCount) {
        UpdateWrapper<LiveRoom> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", liveRoomId).set("viewer_count", viewerCount);
        return this.update(updateWrapper);
    }

    @Override
    public QueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest liveRoomQueryRequest) {
        QueryWrapper<LiveRoom> queryWrapper = new QueryWrapper<>();
        if (liveRoomQueryRequest == null) {
            return queryWrapper;
        }
        
        Long id = liveRoomQueryRequest.getId();
        String title = liveRoomQueryRequest.getTitle();
        Byte status = liveRoomQueryRequest.getStatus();
        Long hostId = liveRoomQueryRequest.getHostId();
        LocalDateTime createTimeStart = liveRoomQueryRequest.getCreateTimeStart();
        LocalDateTime createTimeEnd = liveRoomQueryRequest.getCreateTimeEnd();
        String sortField = liveRoomQueryRequest.getSortField();
        String sortOrder = liveRoomQueryRequest.getSortOrder();
        
        queryWrapper.like(id != null, "id", id);
        queryWrapper.like(title != null && !title.isEmpty(), "title", title);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.eq(hostId != null, "host_id", hostId);
        queryWrapper.ge(createTimeStart != null, "create_time", createTimeStart);
        queryWrapper.le(createTimeEnd != null, "create_time", createTimeEnd);
        
        // 排序
        if (sortField != null && !sortField.isEmpty()) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        } else {
            queryWrapper.orderByDesc("create_time");
        }
        
        return queryWrapper;
    }
}