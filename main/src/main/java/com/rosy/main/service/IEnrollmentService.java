package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Enrollment;

/**
 * 报名Service接口
 */
public interface IEnrollmentService extends IService<Enrollment> {

    /**
     * 分页查询报名记录
     *
     * @param page        分页参数
     * @param activityId  活动ID（可选）
     * @param userId      用户ID（可选）
     * @param status      报名状态（可选）
     * @return 报名记录分页数据
     */
    Page<Enrollment> getEnrollmentPage(Page<Enrollment> page, Long activityId, Long userId, Integer status);

    /**
     * 用户报名活动
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 是否成功
     */
    boolean enrollActivity(Long activityId, Long userId);

    /**
     * 审核报名
     *
     * @param enrollmentId 报名ID
     * @param auditUserId  审核人ID
     * @param status       审核结果
     * @param reason       审核原因（拒绝时必填）
     * @return 是否成功
     */
    boolean auditEnrollment(Long enrollmentId, Long auditUserId, Integer status, String reason);

    /**
     * 取消报名
     *
     * @param enrollmentId 报名ID
     * @return 是否成功
     */
    boolean cancelEnrollment(Long enrollmentId);

    /**
     * 检查用户是否已报名活动
     *
     * @param activityId 活动ID
     * @param userId     用户ID
     * @return 是否已报名
     */
    boolean isUserEnrolled(Long activityId, Long userId);
}