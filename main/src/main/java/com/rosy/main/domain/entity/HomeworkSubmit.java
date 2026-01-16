package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("homework_submit")
public class HomeworkSubmit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("homework_id")
    private Long homeworkId;

    @TableField("student_id")
    private Long studentId;

    @TableField("content")
    private String content;

    @TableField("file_url")
    private String fileUrl;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("score")
    private BigDecimal score;

    @TableField("feedback")
    private String feedback;

    @TableField("status")
    private Byte status;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(value = "updater_id", fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Byte isDeleted;
}