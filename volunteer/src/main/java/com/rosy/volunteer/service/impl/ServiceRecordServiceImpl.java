package com.rosy.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.exception.BusinessException;
import com.rosy.volunteer.domain.dto.ServiceRatingDTO;
import com.rosy.volunteer.domain.entity.Activity;
import com.rosy.volunteer.domain.entity.ActivityRegistration;
import com.rosy.volunteer.domain.entity.ServiceRecord;
import com.rosy.volunteer.domain.vo.ServiceRecordDetailVO;
import com.rosy.volunteer.domain.vo.ServiceRecordListVO;
import com.rosy.volunteer.mapper.ActivityMapper;
import com.rosy.volunteer.mapper.ActivityRegistrationMapper;
import com.rosy.volunteer.mapper.ServiceRecordMapper;
import com.rosy.volunteer.service.IServiceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl implements IServiceRecordService {

    private final ServiceRecordMapper serviceRecordMapper;
    private final ActivityRegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createServiceRecord(Long registrationId) {
        ActivityRegistration registration = registrationMapper.selectById(registrationId);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (registration.getCheckInTime() == null || registration.getCheckOutTime() == null) {
            throw new BusinessException("签到签出时间不完整，无法创建服务记录");
        }
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRecord::getRegistrationId, registrationId);
        if (serviceRecordMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("服务记录已存在");
        }
        ServiceRecord record = new ServiceRecord();
        record.setRegistrationId(registrationId);
        record.setActivityId(registration.getActivityId());
        record.setVolunteerId(registration.getVolunteerId());
        record.setCheckInTime(registration.getCheckInTime());
        record.setCheckOutTime(registration.getCheckOutTime());
        Duration duration = Duration.between(registration.getCheckInTime(), registration.getCheckOutTime());
        record.setServiceDuration((int) duration.toMinutes() / 60);
        record.setCreateTime(LocalDateTime.now());
        serviceRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDuration(Long id) {
        ServiceRecord record = serviceRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }
        ActivityRegistration registration = registrationMapper.selectById(record.getRegistrationId());
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }
        if (registration.getCheckInTime() == null || registration.getCheckOutTime() == null) {
            throw new BusinessException("签到签出时间不完整");
        }
        Duration duration = Duration.between(registration.getCheckInTime(), registration.getCheckOutTime());
        record.setServiceDuration((int) duration.toMinutes() / 60);
        serviceRecordMapper.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rateService(Long id, ServiceRatingDTO dto) {
        ServiceRecord record = serviceRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }
        if (record.getRating() != null) {
            throw new BusinessException("已评价过，无法重复评价");
        }
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new BusinessException("评分必须在1-5之间");
        }
        record.setRating(dto.getRating());
        record.setRatingContent(dto.getRatingContent());
        serviceRecordMapper.updateById(record);
    }

    @Override
    public String generateCertificate(Long id) {
        ServiceRecord record = serviceRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }
        String certificateUrl = "/certificates/" + id + ".pdf";
        record.setCertificateUrl(certificateUrl);
        serviceRecordMapper.updateById(record);
        return certificateUrl;
    }

    @Override
    public ServiceRecordDetailVO getServiceRecordDetail(Long id) {
        ServiceRecord record = serviceRecordMapper.selectById(id);
        if (record == null) {
            throw new BusinessException("服务记录不存在");
        }
        ActivityRegistration registration = registrationMapper.selectById(record.getRegistrationId());
        Activity activity = activityMapper.selectById(record.getActivityId());
        ServiceRecordDetailVO vo = new ServiceRecordDetailVO();
        vo.setId(record.getId());
        vo.setRegistrationId(record.getRegistrationId());
        vo.setActivityId(record.getActivityId());
        vo.setActivityTitle(activity.getTitle());
        vo.setActivityCategory(activity.getCategoryId().toString());
        vo.setActivityTime(activity.getActivityTime());
        vo.setActivityLocation(activity.getLocation());
        vo.setVolunteerId(record.getVolunteerId());
        vo.setVolunteerName(registration.getVolunteerName());
        vo.setVolunteerPhone(registration.getVolunteerPhone());
        vo.setServiceDuration(record.getServiceDuration());
        vo.setRating(record.getRating());
        vo.setRatingContent(record.getRatingContent());
        vo.setCheckInTime(record.getCheckInTime());
        vo.setCheckOutTime(record.getCheckOutTime());
        vo.setCertificateUrl(record.getCertificateUrl());
        vo.setCreateTime(record.getCreateTime());
        return vo;
    }

    @Override
    public List<ServiceRecordListVO> getServiceRecordList(Long activityId, Long volunteerId) {
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(ServiceRecord::getActivityId, activityId);
        }
        if (volunteerId != null) {
            wrapper.eq(ServiceRecord::getVolunteerId, volunteerId);
        }
        wrapper.orderByDesc(ServiceRecord::getCreateTime);
        List<ServiceRecord> records = serviceRecordMapper.selectList(wrapper);
        return records.stream().map(record -> {
            ServiceRecordListVO vo = new ServiceRecordListVO();
            vo.setId(record.getId());
            Activity activity = activityMapper.selectById(record.getActivityId());
            vo.setActivityTitle(activity.getTitle());
            vo.setActivityTime(activity.getActivityTime());
            vo.setServiceDuration(record.getServiceDuration());
            vo.setRating(record.getRating());
            vo.setCreateTime(record.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Object getServiceRecordPage(Long activityId, Long volunteerId, PageRequest pageRequest) {
        Page<ServiceRecord> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(ServiceRecord::getActivityId, activityId);
        }
        if (volunteerId != null) {
            wrapper.eq(ServiceRecord::getVolunteerId, volunteerId);
        }
        wrapper.orderByDesc(ServiceRecord::getCreateTime);
        Page<ServiceRecord> result = serviceRecordMapper.selectPage(page, wrapper);
        List<ServiceRecordListVO> list = result.getRecords().stream().map(record -> {
            ServiceRecordListVO vo = new ServiceRecordListVO();
            vo.setId(record.getId());
            Activity activity = activityMapper.selectById(record.getActivityId());
            vo.setActivityTitle(activity.getTitle());
            vo.setActivityTime(activity.getActivityTime());
            vo.setServiceDuration(record.getServiceDuration());
            vo.setRating(record.getRating());
            vo.setCreateTime(record.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("total", result.getTotal());
        response.put("pageNum", result.getCurrent());
        response.put("pageSize", result.getSize());
        return response;
    }

    @Override
    public Object getVolunteerStatistics(Long volunteerId) {
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRecord::getVolunteerId, volunteerId);
        List<ServiceRecord> records = serviceRecordMapper.selectList(wrapper);
        int totalDuration = records.stream().mapToInt(ServiceRecord::getServiceDuration).sum();
        int activityCount = records.size();
        Double avgRating = records.stream()
                .filter(r -> r.getRating() != null)
                .mapToInt(ServiceRecord::getRating)
                .average()
                .orElse(0.0);
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalDuration", totalDuration);
        statistics.put("activityCount", activityCount);
        statistics.put("avgRating", avgRating);
        return statistics;
    }
}
