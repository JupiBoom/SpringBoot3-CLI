package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.registration.RegistrationQueryRequest;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.vo.RegistrationVO;

public interface IRegistrationService extends IService<Registration> {

    RegistrationVO getRegistrationVO(Registration registration);

    LambdaQueryWrapper<Registration> getQueryWrapper(RegistrationQueryRequest queryRequest);

    boolean register(Long activityId, Long userId);

    boolean audit(Long registrationId, Byte status, String auditRemark, Long auditUserId);

    boolean checkIn(Long activityId, Long userId, String checkInLocation);

    boolean checkOut(Long activityId, Long userId, String checkOutLocation);
}
