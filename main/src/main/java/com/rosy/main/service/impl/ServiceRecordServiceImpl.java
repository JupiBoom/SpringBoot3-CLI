package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ReviewDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.Review;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.ReviewVO;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.RegistrationMapper;
import com.rosy.main.mapper.ReviewMapper;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IServiceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements IServiceRecordService {

    private final RegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;
    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkIn(Long registrationId, Long userId) {
        Registration registration = registrationMapper.selectById(registrationId);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException("无权操作他人的签到");
        }

        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime().minusHours(1))) {
            throw new BusinessException("签到时间过早，请在活动开始前1小时内签到");
        }
        if (now.isAfter(activity.getEndTime())) {
            throw new BusinessException("活动已结束，无法签到");
        }

        ServiceRecord serviceRecord = lambdaQuery()
                .eq(ServiceRecord::getRegistrationId, registrationId)
                .eq(ServiceRecord::getUserId, userId)
                .one();
        if (serviceRecord == null) {
            throw new BusinessException("服务记录不存在");
        }

        if (serviceRecord.getCheckInTime() != null) {
            throw new BusinessException("已签到，请勿重复签到");
        }

        serviceRecord.setCheckInTime(now);
        updateById(serviceRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOut(Long registrationId, Long userId) {
        ServiceRecord serviceRecord = lambdaQuery()
                .eq(ServiceRecord::getRegistrationId, registrationId)
                .eq(ServiceRecord::getUserId, userId)
                .one();
        if (serviceRecord == null) {
            throw new BusinessException("服务记录不存在");
        }

        if (!serviceRecord.getUserId().equals(userId)) {
            throw new BusinessException("无权操作他人的签出");
        }

        if (serviceRecord.getCheckInTime() == null) {
            throw new BusinessException("请先签到后再签出");
        }

        if (serviceRecord.getCheckOutTime() != null) {
            throw new BusinessException("已签出，请勿重复签出");
        }

        LocalDateTime now = LocalDateTime.now();
        serviceRecord.setCheckOutTime(now);

        Duration duration = Duration.between(serviceRecord.getCheckInTime(), now);
        double hours = duration.toMinutes() / 60.0;
        serviceRecord.setServiceHours(Math.round(hours * 100.0) / 100.0);

        updateById(serviceRecord);
    }

    @Override
    public ServiceRecordVO getServiceRecordDetail(Long recordId) {
        ServiceRecord serviceRecord = getById(recordId);
        if (serviceRecord == null) {
            throw new BusinessException("服务记录不存在");
        }
        return BeanUtil.copyProperties(serviceRecord, ServiceRecordVO.class);
    }

    @Override
    public IPage<ServiceRecordVO> getServiceRecordsByUser(Long userId, Integer pageNum, Integer pageSize) {
        Page<ServiceRecord> page = new Page<>(pageNum, pageSize);
        Page<ServiceRecord> result = lambdaQuery()
                .eq(ServiceRecord::getUserId, userId)
                .orderByDesc(ServiceRecord::getCreateTime)
                .page(page);
        return result.convert(r -> BeanUtil.copyProperties(r, ServiceRecordVO.class));
    }

    @Override
    public IPage<ServiceRecordVO> getServiceRecordsByActivity(Long activityId, Integer pageNum, Integer pageSize) {
        Page<ServiceRecord> page = new Page<>(pageNum, pageSize);
        Page<ServiceRecord> result = lambdaQuery()
                .eq(ServiceRecord::getActivityId, activityId)
                .orderByDesc(ServiceRecord::getCreateTime)
                .page(page);
        return result.convert(r -> BeanUtil.copyProperties(r, ServiceRecordVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReview(ReviewDTO dto, Long userId) {
        Activity activity = activityMapper.selectById(dto.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        ServiceRecord serviceRecord = lambdaQuery()
                .eq(ServiceRecord::getActivityId, dto.getActivityId())
                .eq(ServiceRecord::getUserId, userId)
                .one();
        if (serviceRecord == null || serviceRecord.getCheckOutTime() == null) {
            throw new BusinessException("您未完成该活动的服务，无法评价");
        }

        Review existingReview = reviewMapper.selectByActivityAndUser(dto.getActivityId(), userId);
        if (existingReview != null) {
            throw new BusinessException("您已评价过该活动");
        }

        Review review = BeanUtil.copyProperties(dto, Review.class);
        review.setUserId(userId);
        review.setCreatedTime(LocalDateTime.now());
        reviewMapper.insert(review);

        return review.getId();
    }

    @Override
    public IPage<ReviewVO> getReviewsByActivity(Long activityId, Integer pageNum, Integer pageSize) {
        Page<Review> page = new Page<>(pageNum, pageSize);
        Page<Review> result = reviewMapper.selectPage(page,
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Review>()
                        .eq(Review::getActivityId, activityId)
                        .orderByDesc(Review::getCreatedTime));
        return result.convert(r -> BeanUtil.copyProperties(r, ReviewVO.class));
    }

    @Override
    public List<Review> getReviewsByUser(Long userId, Integer pageNum, Integer pageSize) {
        return reviewMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Review>()
                        .eq(Review::getUserId, userId)
                        .orderByDesc(Review::getCreatedTime));
    }

    @Override
    public String generateServiceCertificate(Long recordId, Long userId) {
        ServiceRecord serviceRecord = getById(recordId);
        if (serviceRecord == null) {
            throw new BusinessException("服务记录不存在");
        }

        if (!serviceRecord.getUserId().equals(userId)) {
            throw new BusinessException("无权生成他人的服务证明");
        }

        if (serviceRecord.getCheckOutTime() == null) {
            throw new BusinessException("服务未完成，无法生成证明");
        }

        User user = userMapper.selectById(userId);
        Activity activity = activityMapper.selectById(serviceRecord.getActivityId());

        if (user == null || activity == null) {
            throw new BusinessException("用户或活动信息不存在");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        String certificate = String.format(
                "志愿服务证明\n\n" +
                "兹证明 %s 于 %s 参与了\"%s\"志愿服务活动。\n\n" +
                "活动地点：%s\n" +
                "服务时长：%.2f 小时\n" +
                "服务时间：%s 至 %s\n\n" +
                "特此证明。\n\n" +
                "证明生成时间：%s",
                user.getRealName(),
                activity.getStartTime().format(formatter),
                activity.getTitle(),
                activity.getLocation(),
                serviceRecord.getServiceHours(),
                serviceRecord.getCheckInTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                serviceRecord.getCheckOutTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        return certificate;
    }

    @Override
    public double calculateTotalServiceHours(Long userId) {
        List<ServiceRecord> records = lambdaQuery()
                .eq(ServiceRecord::getUserId, userId)
                .isNotNull(ServiceRecord::getCheckOutTime)
                .list();

        return records.stream()
                .mapToDouble(ServiceRecord::getServiceHours)
                .sum();
    }
}
