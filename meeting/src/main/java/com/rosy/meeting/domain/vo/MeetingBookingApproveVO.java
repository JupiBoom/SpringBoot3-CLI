package com.rosy.meeting.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 会议预约审批VO
 */
@Schema(description = "会议预约审批请求")
public class MeetingBookingApproveVO {

    /**
     * 预约ID
     */
    @Schema(description = "预约ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long bookingId;

    /**
     * 审批状态：1-通过，2-驳回
     */
    @Schema(description = "审批状态：1-通过，2-驳回", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer approvalStatus;

    /**
     * 审批意见
     */
    @Schema(description = "审批意见")
    private String approvalRemark;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalRemark() {
        return approvalRemark;
    }

    public void setApprovalRemark(String approvalRemark) {
        this.approvalRemark = approvalRemark;
    }
}