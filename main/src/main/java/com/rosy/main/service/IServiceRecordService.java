package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ServiceRecordVO;

import java.math.BigDecimal;

public interface IServiceRecordService extends IService<ServiceRecord> {

    ServiceRecordVO getServiceRecordVO(ServiceRecord serviceRecord);

    LambdaQueryWrapper<ServiceRecord> getQueryWrapper(Long activityId, Long userId);

    boolean checkIn(Long activityId, Long userId);

    boolean checkOut(Long activityId, Long userId);

    boolean rateService(Long activityId, Long userId, Byte rating, String comment);

    String generateCertificate(Long activityId, Long userId);
}
