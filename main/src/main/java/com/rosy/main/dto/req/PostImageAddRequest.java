package com.rosy.main.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostImageAddRequest {

    @NotBlank(message = "图片URL不能为空")
    private String imageUrl;
}