package com.rosy.volunteer.service;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.dto.RegistrationCreateDTO;
import com.rosy.volunteer.domain.dto.RegistrationReviewDTO;
import com.rosy.volunteer.domain.vo.RegistrationDetailVO;
import com.rosy.volunteer.domain.vo.RegistrationListVO;

import java.util.List;

public interface IRegistrationService {
    Long createRegistration(RegistrationCreateDTO dto);
    void cancelRegistration(Long id);
    void reviewRegistration(Long id, RegistrationReviewDTO dto);
    void autoReviewRegistration(Long id);
    void checkIn(Long id);
    void checkOut(Long id);
    RegistrationDetailVO getRegistrationDetail(Long id);
    List<RegistrationListVO> getRegistrationList(Long activityId, Integer status, Long volunteerId);
    Object getRegistrationPage(Long activityId, Integer status, Long volunteerId, PageRequest pageRequest);
    void sendActivityReminder(Long activityId);
}
