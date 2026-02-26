package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.servicerecord.ServiceRecordQueryRequest;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ServiceRecordVO;

public interface IServiceRecordService extends IService<ServiceRecord> {

    ServiceRecordVO getServiceRecordVO(ServiceRecord serviceRecord);

    LambdaQueryWrapper<ServiceRecord> getQueryWrapper(ServiceRecordQueryRequest queryRequest);

    boolean rate(Long serviceRecordId, Byte rating, String comment);

    String generateCertificate(Long serviceRecordId);
}
