package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.dto.req.ServiceRecordQueryRequest;
import com.rosy.main.dto.req.ServiceRecordEvaluationRequest;

import java.util.List;

public interface IServiceRecordService extends IService<ServiceRecord> {

    /**
     * 根据ID获取服务记录VO
     */
    ServiceRecordVO getServiceRecordVO(Long id);

    /**
     * 分页查询服务记录
     */
    List<ServiceRecordVO> listServiceRecordVO(ServiceRecordQueryRequest request);

    /**
     * 评价服务记录
     */
    void evaluateServiceRecord(Long id, ServiceRecordEvaluationRequest request);

    /**
     * 生成服务证明
     */
    String generateServiceCertificate(Long id);

    /**
     * 根据志愿者ID获取服务统计
     */
    ServiceRecordVO getVolunteerServiceStats(Long volunteerId);
}