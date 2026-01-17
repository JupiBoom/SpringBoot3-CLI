package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.activity.ActivityRegistrationAddRequest;
import com.rosy.main.domain.dto.activity.ActivityRegistrationAuditRequest;
import com.rosy.main.domain.dto.activity.ActivityRegistrationQueryRequest;
import com.rosy.main.domain.entity.ActivityRegistration;
import com.rosy.main.domain.vo.ActivityRegistrationVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动报名服务接口
 *
 * @author Rosy
 * @since 2026-01-17
 */
public interface IActivityRegistrationService extends IService<ActivityRegistration> {

    /**
     * 报名活动
     */
    Long registerActivity(ActivityRegistrationAddRequest request);

    /**
     * 审核报名
     */
    boolean auditRegistration(Long id, ActivityRegistrationAuditRequest request);

    /**
     * 签到
     */
    boolean signIn(Long registrationId);

    /**
     * 签出
     */
    boolean signOut(Long registrationId);

    /**
     * 获取活动开始前1小时需要提醒的报名
     */
    List<ActivityRegistration> getRemindRegistrations(LocalDateTime activityTime);

    /**
     * 获取VO对象
     */
    ActivityRegistrationVO getActivityRegistrationVO(ActivityRegistration registration);

    /**
     * 获取志愿者的报名列表
     */
    List<ActivityRegistrationVO> getVolunteerRegistrations(Long volunteerId, ActivityRegistrationQueryRequest request);

    /**
     * 获取活动的报名列表
     */
    List<ActivityRegistrationVO> getActivityRegistrations(Long activityId, ActivityRegistrationQueryRequest request);
}