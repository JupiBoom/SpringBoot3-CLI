package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ServiceRecord;

import java.math.BigDecimal;

/**
 * 服务记录Service接口
 */
public interface IServiceRecordService extends IService<ServiceRecord> {

    /**
     * 分页查询服务记录
     *
     * @param page        分页参数
     * @param activityId  活动ID（可选）
     * @param userId      用户ID（可选）
     * @param rating      评价星级（可选）
     * @return 服务记录分页数据
     */
    Page<ServiceRecord> getServiceRecordPage(Page<ServiceRecord> page, Long activityId, Long userId, Integer rating);

    /**
     * 签到
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 是否成功
     */
    boolean checkIn(Long activityId, Long userId);

    /**
     * 签出
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 是否成功
     */
    boolean checkOut(Long activityId, Long userId);

    /**
     * 评价服务
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @param rating     评价星级
     * @param feedback   评价内容
     * @return 是否成功
     */
    boolean rateService(Long activityId, Long userId, Integer rating, String feedback);

    /**
     * 生成服务证明
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 服务证明编号
     */
    String generateCertificate(Long activityId, Long userId);

    /**
     * 计算用户总服务时长
     *
     * @param userId 用户ID
     * @return 总服务时长
     */
    BigDecimal calculateTotalServiceHours(Long userId);

    /**
     * 检查用户是否已签到
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 是否已签到
     */
    boolean isCheckedIn(Long activityId, Long userId);

    /**
     * 检查用户是否已签出
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 是否已签出
     */
    boolean isCheckedOut(Long activityId, Long userId);
}