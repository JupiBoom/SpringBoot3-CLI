package com.rosy.main.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VolunteerUpdateRequest {

    @NotNull(message = "ID不能为空")
    private Long id;
    
    @Size(max = 20, message = "姓名不能超过20字")
    private String name;
    
    private String email;
    
    private Integer gender;
    
    private Integer age;
    
    private String avatar;
}