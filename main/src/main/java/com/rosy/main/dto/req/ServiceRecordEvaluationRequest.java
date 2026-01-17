package com.rosy.main.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceRecordEvaluationRequest {

    @Min(value = 1, message = "评分不能低于1星")
    @Max(value = 5, message = "评分不能高于5星")
    private Integer rating;
    
    @Size(max = 500, message = "评价内容不能超过500字")
    private String evaluation;
}