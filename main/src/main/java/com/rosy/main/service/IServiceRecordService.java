package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ServiceRatingDTO;
import com.rosy.main.domain.entity.ServiceRecord;

import java.util.List;

public interface IServiceRecordService extends IService<ServiceRecord> {
    ServiceRecord checkIn(Long registrationId, Long userId);
    ServiceRecord checkOut(Long registrationId, Long userId);
    void rate(ServiceRatingDTO dto, Long userId);
    String generateCertificate(Long serviceRecordId, Long userId);
    List<ServiceRecord> getMyServiceRecords(Long userId);
}
