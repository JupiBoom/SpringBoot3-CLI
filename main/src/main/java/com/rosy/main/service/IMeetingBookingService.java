package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.vo.MeetingBookingVO;
import com.rosy.main.domain.vo.MeetingBookingRequestVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议预约Service接口
 */
public interface IMeetingBookingService extends IService<MeetingBooking> {

    /**
     * 分页查询预约记录
     * @param page 分页参数
     * @param roomId 会议室ID（可选）
     * @param bookerId 预约人ID（可选）
     * @param status 状态（可选）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @return 分页结果
     */
    Page<MeetingBookingVO> getBookingPage(Page<MeetingBooking> page, Long roomId, Long bookerId, 
                                        Integer status, String startDate, String endDate);

    /**
     * 根据ID获取预约详情
     * @param id 预约ID
     * @return 预约详情
     */
    MeetingBookingVO getBookingById(Long id);

    /**
     * 创建预约
     * @param requestVO 预约请求
     * @param bookerId 预约人ID
     * @return 是否成功
     */
    boolean createBooking(MeetingBookingRequestVO requestVO, Long bookerId);

    /**
     * 更新预约
     * @param requestVO 预约请求
     * @return 是否成功
     */
    boolean updateBooking(MeetingBookingRequestVO requestVO);

    /**
     * 取消预约
     * @param id 预约ID
     * @param bookerId 预约人ID
     * @return 是否成功
     */
    boolean cancelBooking(Long id, Long bookerId);

    /**
     * 审批预约
     * @param id 预约ID
     * @param approveId 审批人ID
     * @param approved 是否通过
     * @param remark 审批备注
     * @return 是否成功
     */
    boolean approveBooking(Long id, Long approveId, boolean approved, String remark);

    /**
     * 检查预约时间冲突
     * @param roomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param excludeId 排除的预约ID（用于更新时检查）
     * @return 是否有冲突
     */
    boolean hasTimeConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId);

    /**
     * 获取用户未读通知数量
     * @param userId 用户ID
     * @return 未读通知数量
     */
    int getUnreadNotificationCount(Long userId);

    /**
     * 获取用户通知列表
     * @param userId 用户ID
     * @return 通知列表
     */
    List<com.rosy.main.domain.vo.NotificationVO> getUserNotifications(Long userId);

    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markNotificationAsRead(Long notificationId, Long userId);

    /**
     * 会议签到
     * @param bookingId 预约ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean checkInMeeting(Long bookingId, Long userId);
}