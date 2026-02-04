package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ServiceRecordQueryRequest;
import com.rosy.main.domain.dto.ServiceRecordReviewRequest;
import com.rosy.main.domain.dto.VolunteerCommentRequest;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ServiceRecordVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 服务记录服务接口
 */
public interface IServiceRecordService extends IService<ServiceRecord> {

    /**
     * 创建服务记录（签到签出后自动创建）
     */
    void createServiceRecord(Registration registration);

    /**
     * 根据ID获取服务记录
     */
    ServiceRecordVO getServiceRecordById(Long id);

    /**
     * 分页查询服务记录
     */
    Page<ServiceRecordVO> listServiceRecords(ServiceRecordQueryRequest request);

    /**
     * 获取用户的服务记录
     */
    List<ServiceRecordVO> getUserServiceRecords(Long userId);

    /**
     * 获取活动的服务记录
     */
    List<ServiceRecordVO> getActivityServiceRecords(Long activityId);

    /**
     * 评价服务记录（组织者评价）
     */
    boolean reviewServiceRecord(ServiceRecordReviewRequest request);

    /**
     * 志愿者评价
     */
    boolean volunteerComment(VolunteerCommentRequest request, Long userId);

    /**
     * 生成服务证明
     */
    String generateCertificate(Long recordId);

    /**
     * 获取用户的累计服务时长
     */
    BigDecimal getUserTotalHours(Long userId);

    /**
     * 获取用户的累计服务次数
     */
    int getUserServiceCount(Long userId);

    /**
     * 获取服务证明PDF
     */
    byte[] getCertificatePdf(Long recordId);
}
