package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ServiceRecordQueryRequest;
import com.rosy.main.domain.dto.ServiceRecordReviewRequest;
import com.rosy.main.domain.dto.VolunteerCommentRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.enums.ActivityCategoryEnum;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.service.IServiceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务记录服务实现类
 */
@Service
@Slf4j
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements IServiceRecordService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public void createServiceRecord(Registration registration) {
        // 计算服务时长
        Duration duration = Duration.between(registration.getCheckInTime(), registration.getCheckOutTime());
        BigDecimal hours = BigDecimal.valueOf(duration.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        ServiceRecord record = new ServiceRecord();
        record.setActivityId(registration.getActivityId());
        record.setUserId(registration.getUserId());
        record.setRegistrationId(registration.getId());
        record.setServiceDate(registration.getCheckInTime().toLocalDate());
        record.setStartTime(registration.getCheckInTime());
        record.setEndTime(registration.getCheckOutTime());
        record.setDurationHours(hours);
        record.setCertificateGenerated(0);

        boolean saved = this.save(record);
        if (!saved) {
            throw new BusinessException("创建服务记录失败");
        }

        log.info("创建服务记录成功，记录ID：{}，用户ID：{}，活动ID：{}，时长：{}小时",
                record.getId(), record.getUserId(), record.getActivityId(), hours);
    }

    @Override
    public ServiceRecordVO getServiceRecordById(Long id) {
        ServiceRecord record = this.getById(id);
        if (record == null) {
            return null;
        }
        return convertToVO(record);
    }

    @Override
    public Page<ServiceRecordVO> listServiceRecords(ServiceRecordQueryRequest request) {
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("service_date");

        if (request.getActivityId() != null) {
            queryWrapper.eq("activity_id", request.getActivityId());
        }
        if (request.getUserId() != null) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (request.getStartDate() != null) {
            queryWrapper.ge("service_date", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            queryWrapper.le("service_date", request.getEndDate());
        }
        if (request.getCertificateGenerated() != null) {
            queryWrapper.eq("certificate_generated", request.getCertificateGenerated());
        }

        Page<ServiceRecord> page = this.page(new Page<>(request.getCurrent(), request.getPageSize()), queryWrapper);

        List<ServiceRecordVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<ServiceRecordVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<ServiceRecordVO> getUserServiceRecords(Long userId) {
        List<ServiceRecord> list = baseMapper.selectByUserId(userId);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceRecordVO> getActivityServiceRecords(Long activityId) {
        List<ServiceRecord> list = baseMapper.selectByActivityId(activityId);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean reviewServiceRecord(ServiceRecordReviewRequest request) {
        ServiceRecord record = this.getById(request.getId());
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }

        record.setPerformance(request.getPerformance());
        record.setOrganizerComment(request.getOrganizerComment());

        return this.updateById(record);
    }

    @Override
    public boolean volunteerComment(VolunteerCommentRequest request, Long userId) {
        ServiceRecord record = this.getById(request.getId());
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }

        // 只能评价自己的服务记录
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException("无权操作");
        }

        record.setVolunteerComment(request.getVolunteerComment());
        record.setServiceContent(request.getServiceContent());

        return this.updateById(record);
    }

    @Override
    public String generateCertificate(Long recordId) {
        ServiceRecord record = this.getById(recordId);
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }

        // 生成证明编号
        String certificateNo = generateCertificateNo(record);

        record.setCertificateNo(certificateNo);
        record.setCertificateGenerated(1);
        record.setCertificateGenerateTime(LocalDateTime.now());

        // 这里可以实现PDF生成功能
        // record.setCertificateUrl(pdfUrl);

        boolean updated = this.updateById(record);
        if (!updated) {
            throw new BusinessException("生成服务证明失败");
        }

        return certificateNo;
    }

    @Override
    public BigDecimal getUserTotalHours(Long userId) {
        return baseMapper.sumDurationHoursByUserId(userId);
    }

    @Override
    public int getUserServiceCount(Long userId) {
        return baseMapper.countByUserId(userId);
    }

    @Override
    public byte[] getCertificatePdf(Long recordId) {
        ServiceRecord record = this.getById(recordId);
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }

        if (record.getCertificateGenerated() != 1) {
            throw new BusinessException("服务证明尚未生成");
        }

        // 这里可以实现PDF生成和返回功能
        // 返回PDF字节数组
        return new byte[0];
    }

    /**
     * 生成证明编号
     */
    private String generateCertificateNo(ServiceRecord record) {
        // 格式：VS-年月日-用户ID后4位-记录ID
        String dateStr = record.getServiceDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String userSuffix = String.format("%04d", record.getUserId() % 10000);
        return String.format("VS-%s-%s-%d", dateStr, userSuffix, record.getId());
    }

    /**
     * 转换为VO
     */
    private ServiceRecordVO convertToVO(ServiceRecord record) {
        ServiceRecordVO vo = new ServiceRecordVO();
        BeanUtil.copyProperties(record, vo);

        // 加载活动信息
        Activity activity = activityMapper.selectById(record.getActivityId());
        if (activity != null) {
            vo.setActivityTitle(activity.getTitle());
            vo.setActivityCategory(activity.getCategory());
            vo.setActivityCategoryDesc(ActivityCategoryEnum.getDescByCode(activity.getCategory()));
            vo.setActivityLocation(activity.getLocation());
        }

        return vo;
    }
}
