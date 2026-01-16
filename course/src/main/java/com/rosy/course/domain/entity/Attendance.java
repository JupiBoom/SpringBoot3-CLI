package com.rosy.course.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 考勤记录表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("attendance")
public class Attendance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 考勤ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 考勤状态：0-签到，1-缺勤，2-请假，3-迟到
     */
    private Byte status;

    /**
     * 签到时间
     */
    private LocalDateTime signTime;

    /**
     * 缺勤原因
     */
    private String absentReason;

    /**
     * 请假开始时间
     */
    private LocalDateTime leaveStartTime;

    /**
     * 请假结束时间
     */
    private LocalDateTime leaveEndTime;

    /**
     * 请假理由
     */
    private String leaveReason;

    /**
     * 请假审批状态：0-待审批，1-已批准，2-已拒绝
     */
    private Byte leaveStatus;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public LocalDateTime getSignTime() {
        return signTime;
    }

    public void setSignTime(LocalDateTime signTime) {
        this.signTime = signTime;
    }

    public String getAbsentReason() {
        return absentReason;
    }

    public void setAbsentReason(String absentReason) {
        this.absentReason = absentReason;
    }

    public LocalDateTime getLeaveStartTime() {
        return leaveStartTime;
    }

    public void setLeaveStartTime(LocalDateTime leaveStartTime) {
        this.leaveStartTime = leaveStartTime;
    }

    public LocalDateTime getLeaveEndTime() {
        return leaveEndTime;
    }

    public void setLeaveEndTime(LocalDateTime leaveEndTime) {
        this.leaveEndTime = leaveEndTime;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public Byte getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(Byte leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public LocalDateTime getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(LocalDateTime approveTime) {
        this.approveTime = approveTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }
}