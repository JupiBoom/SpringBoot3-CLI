package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.leave.LeaveApproveRequest;
import com.rosy.main.domain.dto.leave.LeaveQueryRequest;
import com.rosy.main.domain.entity.LeaveRequest;
import com.rosy.main.domain.vo.LeaveRequestVO;

/**
 * <p>
 * 请假表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
public interface ILeaveRequestService extends IService<LeaveRequest> {

    /**
     * 获取查询条件
     *
     * @param leaveQueryRequest 请假查询请求
     * @return 查询条件
     */
    QueryWrapper<LeaveRequest> getQueryWrapper(LeaveQueryRequest leaveQueryRequest);

    /**
     * 获取请假视图对象
     *
     * @param leaveRequest 请假实体
     * @return 请假视图对象
     */
    LeaveRequestVO getLeaveRequestVO(LeaveRequest leaveRequest);

    /**
     * 分页获取请假视图对象
     *
     * @param leaveRequestPage 请假分页对象
     * @return 请假视图对象分页
     */
    Page<LeaveRequestVO> getLeaveRequestVOPage(Page<LeaveRequest> leaveRequestPage);

    /**
     * 审批请假
     *
     * @param leaveApproveRequest 请假审批请求
     * @param approverId 审批人ID
     * @return 是否成功
     */
    boolean approveLeave(LeaveApproveRequest leaveApproveRequest, Long approverId);

    /**
     * 学生请假
     *
     * @param studentId 学生ID
     * @param courseId 课程ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param reason 请假原因
     * @return 是否成功
     */
    boolean studentLeave(Long studentId, Long courseId, String startDate, String endDate, String reason);
}