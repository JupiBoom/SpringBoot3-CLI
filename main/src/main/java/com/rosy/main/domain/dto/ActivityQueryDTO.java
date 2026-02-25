package com.rosy.main.domain.dto;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityQueryDTO extends PageRequest {
    private String keyword;
    private String category;
    private String status;
}
