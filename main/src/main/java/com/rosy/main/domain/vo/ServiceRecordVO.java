package com.rosy.main.domain.vo;

import com.rosy.main.common.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceRecordVO extends BaseVO {

    private Long id;
    
    private Long activityId;
    
    private Long volunteerId;
    
    private String volunteerName;
    
    private String activityName;
    
    private String activityCategory;
    
    private String activityCategoryName;
    
    private LocalDateTime signInTime;
    
    private LocalDateTime signOutTime;
    
    private String serviceDuration;
    
    private Integer rating;
    
    private String evaluation;
    
    private String totalServiceDuration;
    
    private Integer serviceCount;
    
    private BigDecimal averageRating;
}