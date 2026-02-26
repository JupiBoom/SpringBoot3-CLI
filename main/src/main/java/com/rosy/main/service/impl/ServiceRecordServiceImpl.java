package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.servicerecord.ServiceRecordQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IServiceRecordService;
import com.rosy.main.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements IServiceRecordService {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IUserService userService;

    @Override
    public ServiceRecordVO getServiceRecordVO(ServiceRecord serviceRecord) {
        if (serviceRecord == null) {
            return null;
        }
        ServiceRecordVO vo = BeanUtil.copyProperties(serviceRecord, ServiceRecordVO.class);

        if (serviceRecord.getActivityId() != null) {
            Activity activity = activityMapper.selectById(serviceRecord.getActivityId());
            if (activity != null) {
                vo.setActivityTitle(activity.getTitle());
            }
        }

        if (serviceRecord.getUserId() != null) {
            User user = userMapper.selectById(serviceRecord.getUserId());
            if (user != null) {
                vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
            }
        }

        return vo;
    }

    @Override
    public LambdaQueryWrapper<ServiceRecord> getQueryWrapper(ServiceRecordQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<ServiceRecord> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), ServiceRecord::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getActivityId(), ServiceRecord::getActivityId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), ServiceRecord::getUserId);

        if (queryRequest.getServiceDateBegin() != null) {
            queryWrapper.ge(ServiceRecord::getServiceDate, queryRequest.getServiceDateBegin());
        }
        if (queryRequest.getServiceDateEnd() != null) {
            queryWrapper.le(ServiceRecord::getServiceDate, queryRequest.getServiceDateEnd());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                ServiceRecord::getServiceDate);

        return queryWrapper;
    }

    @Override
    public boolean rate(Long serviceRecordId, Byte rating, String comment) {
        ServiceRecord serviceRecord = this.getById(serviceRecordId);
        if (serviceRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "服务记录不存在");
        }

        serviceRecord.setRating(rating);
        serviceRecord.setComment(comment);
        serviceRecord.setRatingTime(LocalDateTime.now());

        return this.updateById(serviceRecord);
    }

    @Override
    public String generateCertificate(Long serviceRecordId) {
        ServiceRecord serviceRecord = this.getById(serviceRecordId);
        if (serviceRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "服务记录不存在");
        }

        if (serviceRecord.getCertificateUrl() != null) {
            return serviceRecord.getCertificateUrl();
        }

        String certificateNo = "CERT-" + IdUtil.getSnowflakeNextIdStr();
        String certificateUrl = "/certificates/" + certificateNo + ".pdf";

        serviceRecord.setCertificateUrl(certificateUrl);
        this.updateById(serviceRecord);

        return certificateUrl;
    }
}
