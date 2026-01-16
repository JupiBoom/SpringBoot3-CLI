package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.LiveData;
import com.rosy.main.domain.vo.LiveDataVO;
import com.rosy.main.mapper.LiveDataMapper;
import com.rosy.main.service.ILiveDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 直播间数据表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Service
public class LiveDataServiceImpl extends ServiceImpl<LiveDataMapper, LiveData> implements ILiveDataService {

    @Override
    public LiveDataVO getLiveData(Long roomId) {
        LambdaQueryWrapper<LiveData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveData::getLiveRoomId, roomId);
        LiveData liveData = this.getOne(queryWrapper);
        if (liveData == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据记录不存在");
        }
        return this.toVO(liveData);
    }

    @Override
    @Transactional
    public void incrementViewCount(Long roomId) {
        LiveData liveData = this.getLiveDataByRoomId(roomId);
        liveData.setViewCount(liveData.getViewCount() + 1);
        // 更新在线人数
        liveData.setCurrentViewCount(liveData.getCurrentViewCount() + 1);
        // 更新峰值人数
        if (liveData.getCurrentViewCount() > liveData.getPeakViewCount()) {
            liveData.setPeakViewCount(liveData.getCurrentViewCount());
        }
        this.updateById(liveData);
    }

    @Override
    @Transactional
    public void incrementOrderCount(Long roomId, BigDecimal amount) {
        LiveData liveData = this.getLiveDataByRoomId(roomId);
        liveData.setOrderCount(liveData.getOrderCount() + 1);
        liveData.setSalesAmount(liveData.getSalesAmount().add(amount));
        this.updateById(liveData);
    }

    @Override
    @Transactional
    public void calculateConversionRate(Long roomId) {
        LiveData liveData = this.getLiveDataByRoomId(roomId);
        if (liveData.getViewCount() > 0) {
            BigDecimal rate = new BigDecimal(liveData.getOrderCount())
                    .divide(new BigDecimal(liveData.getViewCount()), 2, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100));
            liveData.setConversionRate(rate);
            this.updateById(liveData);
        }
    }

    @Override
    public LiveDataVO toVO(LiveData liveData) {
        return BeanUtil.copyProperties(liveData, LiveDataVO.class);
    }

    private LiveData getLiveDataByRoomId(Long roomId) {
        LambdaQueryWrapper<LiveData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveData::getLiveRoomId, roomId);
        LiveData liveData = this.getOne(queryWrapper);
        if (liveData == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据记录不存在");
        }
        return liveData;
    }
}
