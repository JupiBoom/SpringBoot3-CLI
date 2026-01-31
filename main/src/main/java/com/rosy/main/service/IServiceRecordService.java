package com.rosy.main.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ReviewDTO;
import com.rosy.main.domain.entity.Review;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.ReviewVO;
import com.rosy.main.domain.vo.ServiceRecordVO;

import java.util.List;

public interface IServiceRecordService extends IService<ServiceRecord> {

    void checkIn(Long registrationId, Long userId);

    void checkOut(Long registrationId, Long userId);

    ServiceRecordVO getServiceRecordDetail(Long recordId);

    IPage<ServiceRecordVO> getServiceRecordsByUser(Long userId, Integer pageNum, Integer pageSize);

    IPage<ServiceRecordVO> getServiceRecordsByActivity(Long activityId, Integer pageNum, Integer pageSize);

    Long createReview(ReviewDTO dto, Long userId);

    IPage<ReviewVO> getReviewsByActivity(Long activityId, Integer pageNum, Integer pageSize);

    List<Review> getReviewsByUser(Long userId, Integer pageNum, Integer pageSize);

    String generateServiceCertificate(Long recordId, Long userId);

    double calculateTotalServiceHours(Long userId);
}
