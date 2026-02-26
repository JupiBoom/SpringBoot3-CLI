package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("audience_record")
public class AudienceRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long liveRoomId;

    private Integer viewerCount;

    private Integer newViewerCount;

    private Integer leaveViewerCount;

    private LocalDateTime recordTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
