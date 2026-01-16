package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("attendance")
public class Attendance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("course_id")
    private Long courseId;

    @TableField("student_id")
    private Long studentId;

    @TableField("attendance_date")
    private LocalDate attendanceDate;

    @TableField("status")
    private Byte status;

    @TableField("leave_type")
    private Byte leaveType;

    @TableField("leave_reason")
    private String leaveReason;

    @TableField("leave_status")
    private Byte leaveStatus;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updater_id", fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}