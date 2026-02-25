package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.RegistrationAuditDTO;
import com.rosy.main.domain.entity.Registration;

import java.util.List;

public interface IRegistrationService extends IService<Registration> {
    Registration register(Long activityId, Long userId);
    void audit(RegistrationAuditDTO dto, Long auditorId);
    void cancel(Long registrationId, Long userId);
    List<Registration> getMyRegistrations(Long userId);
    List<Registration> getActivityRegistrations(Long activityId);
}
