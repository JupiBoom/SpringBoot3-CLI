package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveData;
import com.rosy.main.domain.vo.LiveDataVO;

/**
 * <p>
 * 直播间数据表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
public interface ILiveDataService extends IService<LiveData> {

    /**
     * 获取直播间数据
     *
     * @param roomId 直播间ID
     * @return 数据VO
     */
    LiveDataVO getLiveData(Long roomId);

    /**
     * 新增观众
     *
     * @param roomId 直播间ID
     */
    void incrementViewCount(Long roomId);

    /**
     * 新增订单
     *
     * @param roomId    直播间ID
     * @param amount    订单金额
     */
    void incrementOrderCount(Long roomId, java.math.BigDecimal amount);

    /**
     * 计算转化率
     *
     * @param roomId 直播间ID
     */
    void calculateConversionRate(Long roomId);

    /**
     * 转换为VO
     *
     * @param liveData 数据实体
     * @return 数据VO
     */
    LiveDataVO toVO(LiveData liveData);
}
