package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Registration;

public interface IRegistrationService extends IService<Registration> {

    LambdaQueryWrapper<Registration> getQueryWrapper(Long activityId, Long userId, Byte status);

    boolean registerActivity(Long activityId, Long userId, Byte auditType);

    boolean auditRegistration(Long registrationId, Long auditorId, Byte status, String remark);

    boolean checkDuplicateRegistration(Long activityId, Long userId);
}
