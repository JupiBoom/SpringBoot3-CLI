package com.rosy.volunteer.service;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.dto.ServiceRatingDTO;
import com.rosy.volunteer.domain.vo.ServiceRecordDetailVO;
import com.rosy.volunteer.domain.vo.ServiceRecordListVO;

import java.util.List;

public interface IServiceRecordService {
    void createServiceRecord(Long registrationId);
    void updateDuration(Long id);
    void rateService(Long id, ServiceRatingDTO dto);
    String generateCertificate(Long id);
    ServiceRecordDetailVO getServiceRecordDetail(Long id);
    List<ServiceRecordListVO> getServiceRecordList(Long activityId, Long volunteerId);
    Object getServiceRecordPage(Long activityId, Long volunteerId, PageRequest pageRequest);
    Object getVolunteerStatistics(Long volunteerId);
}
