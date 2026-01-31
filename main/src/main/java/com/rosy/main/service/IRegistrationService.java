package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.RegistrationDTO;
import com.rosy.main.domain.dto.RegistrationReviewDTO;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.vo.RegistrationVO;

public interface IRegistrationService extends IService<Registration> {

    Long createRegistration(RegistrationDTO dto, Long userId);

    void autoReviewRegistration(Long registrationId);

    void manualReviewRegistration(RegistrationReviewDTO dto, Long reviewerId);

    RegistrationVO getRegistrationDetail(Long registrationId);

    Page<RegistrationVO> getRegistrationByActivity(Long activityId, Integer pageNum, Integer pageSize);

    Page<RegistrationVO> getRegistrationByUser(Long userId, Integer pageNum, Integer pageSize);

    void cancelRegistration(Long registrationId, Long userId);
}
