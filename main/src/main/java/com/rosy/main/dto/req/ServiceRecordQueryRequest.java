package com.rosy.main.dto.req;

import com.rosy.main.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceRecordQueryRequest extends PageRequest {

    private Long volunteerId;
    
    private Long activityId;
    
    private LocalDateTime startSignInTime;
    
    private LocalDateTime endSignInTime;
    
    private String activityCategory;
}