package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.entity.Volunteer;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.dto.req.ServiceRecordQueryRequest;
import com.rosy.main.dto.req.ServiceRecordEvaluationRequest;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.mapper.VolunteerMapper;
import com.rosy.main.service.IServiceRecordService;
import com.rosy.main.enums.ActivityCategoryEnum;
import com.rosy.main.enums.ActivityStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements IServiceRecordService {

    private final ActivityMapper activityMapper;
    private final VolunteerMapper volunteerMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ServiceRecordVO getServiceRecordVO(Long id) {
        ServiceRecord serviceRecord = getById(id);
        if (serviceRecord == null) {
            return null;
        }
        return toVO(serviceRecord);
    }

    @Override
    public List<ServiceRecordVO> listServiceRecordVO(ServiceRecordQueryRequest request) {
        LambdaQueryWrapper<ServiceRecord> wrapper = getQueryWrapper(request);
        Page<ServiceRecord> page = new Page<>(request.getPageNum(), request.getPageSize());
        page(page, wrapper);
        return page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void evaluateServiceRecord(Long id, ServiceRecordEvaluationRequest request) {
        ServiceRecord serviceRecord = getById(id);
        if (serviceRecord == null) {
            throw new RuntimeException("服务记录不存在");
        }
        if (serviceRecord.getSignOutTime() == null) {
            throw new RuntimeException("活动尚未结束，无法评价");
        }
        serviceRecord.setRating(request.getRating());
        serviceRecord.setEvaluation(request.getEvaluation());
        updateById(serviceRecord);
    }

    @Override
    public String generateServiceCertificate(Long id) {
        ServiceRecordVO serviceRecordVO = getServiceRecordVO(id);
        if (serviceRecordVO == null) {
            throw new RuntimeException("服务记录不存在");
        }

        StringBuilder certificate = new StringBuilder();
        certificate.append("\n");
        certificate.append("===========================================\n");
        certificate.append("           志愿者服务证明\n");
        certificate.append("===========================================\n\n");
        certificate.append("志愿者姓名：").append(serviceRecordVO.getVolunteerName()).append("\n");
        certificate.append("服务活动：").append(serviceRecordVO.getActivityName()).append("\n");
        certificate.append("活动分类：").append(serviceRecordVO.getActivityCategoryName()).append("\n");
        certificate.append("签到时间：").append(serviceRecordVO.getSignInTime().format(FORMATTER)).append("\n");
        certificate.append("签出时间：").append(serviceRecordVO.getSignOutTime().format(FORMATTER)).append("\n");
        certificate.append("服务时长：").append(serviceRecordVO.getServiceDuration()).append("\n");
        if (serviceRecordVO.getRating() != null) {
            certificate.append("服务评价：").append("★".repeat(serviceRecordVO.getRating())).append("\n");
            if (StrUtil.isNotBlank(serviceRecordVO.getEvaluation())) {
                certificate.append("评价内容：").append(serviceRecordVO.getEvaluation()).append("\n");
            }
        }
        certificate.append("\n");
        certificate.append("  特此证明\n\n");
        certificate.append("  生成时间：").append(LocalDateTime.now().format(FORMATTER)).append("\n");
        certificate.append("===========================================\n");

        return certificate.toString();
    }

    @Override
    public ServiceRecordVO getVolunteerServiceStats(Long volunteerId) {
        Volunteer volunteer = volunteerMapper.selectById(volunteerId);
        if (volunteer == null) {
            throw new RuntimeException("志愿者不存在");
        }

        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRecord::getVolunteerId, volunteerId);
        List<ServiceRecord> records = list(wrapper);

        long totalHours = 0;
        int serviceCount = records.size();
        int totalRating = 0;
        int ratedCount = 0;

        for (ServiceRecord record : records) {
            String duration = record.getServiceDuration();
            if (duration != null) {
                String[] parts = duration.split("小时");
                if (parts.length > 0) {
                    try {
                        totalHours += Long.parseLong(parts[0].trim());
                    } catch (NumberFormatException e) {
                        log.warn("解析时长失败: {}", duration);
                    }
                }
            }
            if (record.getRating() != null) {
                totalRating += record.getRating();
                ratedCount++;
            }
        }

        BigDecimal avgRating = ratedCount > 0 
                ? new BigDecimal(totalRating).divide(new BigDecimal(ratedCount), 1, RoundingMode.HALF_UP) 
                : BigDecimal.ZERO;

        ServiceRecordVO stats = new ServiceRecordVO();
        stats.setVolunteerId(volunteerId);
        stats.setVolunteerName(volunteer.getName());
        stats.setTotalServiceDuration(totalHours + "小时");
        stats.setServiceCount(serviceCount);
        stats.setAverageRating(avgRating);

        return stats;
    }

    private LambdaQueryWrapper<ServiceRecord> getQueryWrapper(ServiceRecordQueryRequest request) {
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        if (request.getVolunteerId() != null) {
            wrapper.eq(ServiceRecord::getVolunteerId, request.getVolunteerId());
        }
        if (request.getActivityId() != null) {
            wrapper.eq(ServiceRecord::getActivityId, request.getActivityId());
        }
        if (request.getStartSignInTime() != null) {
            wrapper.ge(ServiceRecord::getSignInTime, request.getStartSignInTime());
        }
        if (request.getEndSignInTime() != null) {
            wrapper.le(ServiceRecord::getSignInTime, request.getEndSignInTime());
        }
        if (StrUtil.isNotBlank(request.getActivityCategory())) {
            wrapper.eq(ServiceRecord::getActivityCategory, request.getActivityCategory());
        }
        wrapper.orderByDesc(ServiceRecord::getSignInTime);
        return wrapper;
    }

    private ServiceRecordVO toVO(ServiceRecord serviceRecord) {
        ServiceRecordVO vo = new ServiceRecordVO();
        BeanUtil.copyProperties(serviceRecord, vo);
        
        String category = serviceRecord.getActivityCategory();
        if (StrUtil.isNotBlank(category)) {
            try {
                vo.setActivityCategoryName(ActivityCategoryEnum.fromCode(Integer.parseInt(category)).getDesc());
            } catch (IllegalArgumentException e) {
                vo.setActivityCategoryName(category);
            }
        }

        return vo;
    }
}