package com.rosy.main.domain.vo;

import com.rosy.main.common.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class VolunteerVO extends BaseVO {

    private Long id;
    
    private String name;
    
    private String phone;
    
    private String email;
    
    private Integer gender;
    
    private Integer age;
    
    private String avatar;
    
    private Long totalServiceDuration;
    
    private Integer serviceCount;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
}