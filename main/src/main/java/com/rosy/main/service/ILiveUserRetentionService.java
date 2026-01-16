package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveUserRetention;
import com.rosy.main.domain.vo.LiveUserRetentionVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 观众留存记录表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
public interface ILiveUserRetentionService extends IService<LiveUserRetention> {

    /**
     * 记录观众进入
     *
     * @param roomId 直播间ID
     * @param userId 用户ID（可选）
     * @return 记录ID
     */
    Long recordUserEnter(Long roomId, Long userId);

    /**
     * 记录观众离开
     *
     * @param recordId 记录ID
     */
    void recordUserLeave(Long recordId);

    /**
     * 获取观众留存曲线数据
     *
     * @param roomId 直播间ID
     * @return 留存数据（时间 -> 在线人数）
     */
    Map<LocalDateTime, Integer> getRetentionCurve(Long roomId);

    /**
     * 计算平均停留时长
     *
     * @param roomId 直播间ID
     * @return 平均停留时长（秒）
     */
    Double getAverageDuration(Long roomId);

    /**
     * 转换为VO
     *
     * @param retention 留存记录
     * @return 留存VO
     */
    LiveUserRetentionVO toVO(LiveUserRetention retention);
}
