package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.liveRoom.LiveRoomAddRequest;
import com.rosy.main.domain.dto.liveRoom.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.liveRoom.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;

/**
 * <p>
 * 直播间表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
public interface ILiveRoomService extends IService<LiveRoom> {

    /**
     * 创建直播间
     * @param liveRoomAddRequest 直播间添加请求
     * @return 直播间ID
     */
    Long createLiveRoom(LiveRoomAddRequest liveRoomAddRequest);

    /**
     * 更新直播间
     * @param liveRoomUpdateRequest 直播间更新请求
     * @return 是否成功
     */
    boolean updateLiveRoom(LiveRoomUpdateRequest liveRoomUpdateRequest);

    /**
     * 获取直播间视图对象
     * @param liveRoom 直播间实体
     * @return 直播间视图对象
     */
    LiveRoomVO getLiveRoomVO(LiveRoom liveRoom);

    /**
     * 分页获取直播间列表
     * @param liveRoomQueryRequest 查询请求
     * @return 分页结果
     */
    Page<LiveRoomVO> listLiveRoomVOByPage(LiveRoomQueryRequest liveRoomQueryRequest);

    /**
     * 开始直播
     * @param liveRoomId 直播间ID
     * @return 是否成功
     */
    boolean startLive(Long liveRoomId);

    /**
     * 结束直播
     * @param liveRoomId 直播间ID
     * @return 是否成功
     */
    boolean endLive(Long liveRoomId);

    /**
     * 更新直播间观众人数
     * @param liveRoomId 直播间ID
     * @param viewerCount 观众人数
     * @return 是否成功
     */
    boolean updateViewerCount(Long liveRoomId, Integer viewerCount);

    /**
     * 获取查询条件
     * @param liveRoomQueryRequest 查询请求
     * @return 查询条件
     */
    com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest liveRoomQueryRequest);
}