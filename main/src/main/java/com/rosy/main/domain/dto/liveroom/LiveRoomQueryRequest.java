package com.rosy.main.domain.dto.liveroom;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class LiveRoomQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    @Positive(message = "主播ID必须为正整数")
    private Long anchorId;

    @Min(value = 0, message = "状态值只能为0-2")
    @Max(value = 2, message = "状态值只能为0-2")
    private Byte status;

    private LocalDateTime startTimeBegin;

    private LocalDateTime startTimeEnd;
}
