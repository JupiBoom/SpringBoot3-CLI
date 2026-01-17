package com.rosy.main.dto.req;

import com.rosy.main.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ForumPostQueryRequest extends PageRequest {

    private Long activityId;
    
    private String keyword;
}