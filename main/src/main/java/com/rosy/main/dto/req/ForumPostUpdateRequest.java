package com.rosy.main.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForumPostUpdateRequest {

    @NotNull(message = "ID不能为空")
    private Long id;
    
    @Size(max = 100, message = "标题不能超过100字")
    private String title;
    
    @Size(max = 2000, message = "内容不能超过2000字")
    private String content;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
}