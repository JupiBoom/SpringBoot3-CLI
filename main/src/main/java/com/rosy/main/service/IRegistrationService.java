package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.CheckInRequest;
import com.rosy.main.domain.dto.RegistrationCreateRequest;
import com.rosy.main.domain.dto.RegistrationQueryRequest;
import com.rosy.main.domain.dto.RegistrationReviewRequest;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.vo.RegistrationVO;

import java.util.List;

/**
 * 报名服务接口
 */
public interface IRegistrationService extends IService<Registration> {

    /**
     * 报名活动
     */
    Long register(RegistrationCreateRequest request, Long userId);

    /**
     * 取消报名
     */
    boolean cancelRegistration(Long registrationId, Long userId);

    /**
     * 审核报名
     */
    boolean reviewRegistration(RegistrationReviewRequest request, Long reviewerId);

    /**
     * 签到
     */
    boolean checkIn(CheckInRequest request);

    /**
     * 签出
     */
    boolean checkOut(CheckInRequest request);

    /**
     * 根据ID获取报名详情
     */
    RegistrationVO getRegistrationById(Long id);

    /**
     * 分页查询报名列表
     */
    Page<RegistrationVO> listRegistrations(RegistrationQueryRequest request);

    /**
     * 获取用户的报名列表
     */
    List<RegistrationVO> getUserRegistrations(Long userId);

    /**
     * 获取活动的报名列表
     */
    List<RegistrationVO> getActivityRegistrations(Long activityId);

    /**
     * 发送活动提醒
     */
    void sendActivityReminder(Long registrationId);

    /**
     * 获取需要发送提醒的报名列表（定时任务使用）
     */
    List<Registration> getNeedReminderList();

    /**
     * 检查用户是否已报名活动
     */
    boolean hasRegistered(Long activityId, Long userId);
}
