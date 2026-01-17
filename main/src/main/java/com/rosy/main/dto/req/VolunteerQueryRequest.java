package com.rosy.main.dto.req;

import com.rosy.main.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VolunteerQueryRequest extends PageRequest {

    private String name;
    
    private String phone;
    
    private Integer gender;
    
    private Integer minAge;
    
    private Integer maxAge;
}