package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RepairRecordAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "报修单ID不能为空")
    private Long orderId;

    @NotNull(message = "记录类型不能为空")
    private Byte recordType;

    @NotBlank(message = "记录内容不能为空")
    @Size(max = 1000, message = "记录内容长度不能超过1000个字符")
    private String content;

    private List<String> photoUrls;
}
