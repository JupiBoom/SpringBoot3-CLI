package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveUserRetention;
import com.rosy.main.domain.vo.LiveUserRetentionVO;
import com.rosy.main.mapper.LiveUserRetentionMapper;
import com.rosy.main.service.ILiveUserRetentionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 观众留存记录表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Service
public class LiveUserRetentionServiceImpl extends ServiceImpl<LiveUserRetentionMapper, LiveUserRetention> implements ILiveUserRetentionService {

    @Override
    @Transactional
    public Long recordUserEnter(Long roomId, Long userId) {
        LiveUserRetention retention = new LiveUserRetention();
        retention.setLiveRoomId(roomId);
        retention.setUserId(userId);
        retention.setEnterTime(LocalDateTime.now());
        retention.setCreateTime(LocalDateTime.now());
        this.save(retention);
        return retention.getId();
    }

    @Override
    @Transactional
    public void recordUserLeave(Long recordId) {
        LiveUserRetention retention = this.getById(recordId);
        if (retention != null && retention.getLeaveTime() == null) {
            retention.setLeaveTime(LocalDateTime.now());
            retention.setDuration((int) Duration.between(retention.getEnterTime(), retention.getLeaveTime()).getSeconds());
            this.updateById(retention);
        }
    }

    @Override
    public Map<LocalDateTime, Integer> getRetentionCurve(Long roomId) {
        LambdaQueryWrapper<LiveUserRetention> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveUserRetention::getLiveRoomId, roomId)
                .orderByAsc(LiveUserRetention::getEnterTime);
        List<LiveUserRetention> retentionList = this.list(queryWrapper);

        // 按时间分组统计在线人数
        Map<LocalDateTime, Integer> curve = new HashMap<>();
        for (LiveUserRetention retention : retentionList) {
            // 进入时间点+1
            curve.merge(retention.getEnterTime(), 1, Integer::sum);
            // 离开时间点-1（如果有离开时间）
            if (retention.getLeaveTime() != null) {
                curve.merge(retention.getLeaveTime(), -1, Integer::sum);
            }
        }

        // 计算累计在线人数
        Map<LocalDateTime, Integer> result = new HashMap<>();
        int currentOnline = 0;
        for (LocalDateTime time : curve.keySet().stream().sorted().collect(Collectors.toList())) {
            currentOnline += curve.get(time);
            result.put(time, currentOnline);
        }

        return result;
    }

    @Override
    public Double getAverageDuration(Long roomId) {
        LambdaQueryWrapper<LiveUserRetention> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveUserRetention::getLiveRoomId, roomId)
                .isNotNull(LiveUserRetention::getDuration);
        List<LiveUserRetention> retentionList = this.list(queryWrapper);

        if (retentionList.isEmpty()) {
            return 0.0;
        }

        long totalDuration = retentionList.stream()
                .mapToLong(LiveUserRetention::getDuration)
                .sum();

        return (double) totalDuration / retentionList.size();
    }

    @Override
    public LiveUserRetentionVO toVO(LiveUserRetention retention) {
        return BeanUtil.copyProperties(retention, LiveUserRetentionVO.class);
    }
}
