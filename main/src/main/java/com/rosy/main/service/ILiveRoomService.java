package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomCreateVO;
import com.rosy.main.domain.vo.LiveRoomVO;

/**
 * <p>
 * 直播间表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
public interface ILiveRoomService extends IService<LiveRoom> {

    /**
     * 创建直播间
     *
     * @param liveRoom 直播间信息
     * @return 直播间VO
     */
    LiveRoomVO createLiveRoom(LiveRoomCreateVO liveRoomCreateVO);

    /**
     * 开始直播
     *
     * @param roomId 直播间ID
     * @return 直播间VO
     */
    LiveRoomVO startLive(Long roomId);

    /**
     * 结束直播
     *
     * @param roomId 直播间ID
     * @return 直播间VO
     */
    LiveRoomVO endLive(Long roomId);

    /**
     * 切换讲解商品
     *
     * @param roomId  直播间ID
     * @param goodsId 商品ID
     * @return 直播间VO
     */
    LiveRoomVO switchCurrentGoods(Long roomId, Long goodsId);

    /**
     * 获取直播间详情
     *
     * @param roomId 直播间ID
     * @return 直播间VO
     */
    LiveRoomVO getLiveRoomDetail(Long roomId);

    /**
     * 转换为VO
     *
     * @param liveRoom 直播间实体
     * @return 直播间VO
     */
    LiveRoomVO toVO(LiveRoom liveRoom);
}
