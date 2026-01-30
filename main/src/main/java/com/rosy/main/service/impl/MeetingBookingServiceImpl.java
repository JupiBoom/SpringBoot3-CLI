package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.*;
import com.rosy.main.domain.vo.MeetingBookingRequestVO;
import com.rosy.main.domain.vo.MeetingBookingVO;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.mapper.*;
import com.rosy.main.service.IMeetingBookingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议预约Service实现类
 */
@Service
public class MeetingBookingServiceImpl extends ServiceImpl<MeetingBookingMapper, MeetingBooking> implements IMeetingBookingService {

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MeetingCheckinMapper meetingCheckinMapper;
    
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public Page<MeetingBookingVO> getBookingPage(Page<MeetingBooking> page, Long roomId, Long bookerId, 
                                               Integer status, String startDate, String endDate) {
        QueryWrapper<MeetingBooking> queryWrapper = new QueryWrapper<>();
        
        if (roomId != null) {
            queryWrapper.eq("room_id", roomId);
        }
        
        if (bookerId != null) {
            queryWrapper.eq("booker_id", bookerId);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        if (StringUtils.hasText(startDate)) {
            queryWrapper.ge("start_time", startDate + " 00:00:00");
        }
        
        if (StringUtils.hasText(endDate)) {
            queryWrapper.le("start_time", endDate + " 23:59:59");
        }
        
        queryWrapper.orderByDesc("create_time");
        
        Page<MeetingBooking> bookingPage = this.page(page, queryWrapper);
        
        // 转换为VO
        Page<MeetingBookingVO> voPage = new Page<>();
        BeanUtils.copyProperties(bookingPage, voPage);
        
        List<MeetingBookingVO> voList = bookingPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        voPage.setRecords(voList);
        
        return voPage;
    }

    @Override
    public MeetingBookingVO getBookingById(Long id) {
        MeetingBooking booking = this.getById(id);
        if (booking == null) {
            return null;
        }
        return convertToVO(booking);
    }

    @Override
    @Transactional
    public boolean createBooking(MeetingBookingRequestVO requestVO, Long bookerId) {
        // 检查会议室是否存在
        MeetingRoom room = meetingRoomMapper.selectById(requestVO.getRoomId());
        if (room == null || room.getStatus() != 1) {
            throw new RuntimeException("会议室不存在或不可用");
        }
        
        // 解析时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(requestVO.getStartTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestVO.getEndTime(), formatter);
        
        // 检查时间是否合理
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("开始时间不能晚于结束时间");
        }
        
        // 检查时间冲突
        if (hasTimeConflict(requestVO.getRoomId(), startTime, endTime, null)) {
            throw new RuntimeException("该时间段已被预约");
        }
        
        // 创建预约
        MeetingBooking booking = new MeetingBooking();
        BeanUtils.copyProperties(requestVO, booking);
        booking.setBookerId(bookerId);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setStatus(0); // 待审批
        
        boolean result = this.save(booking);
        
        if (result) {
            // 发送通知给管理员
            sendNotificationToAdmins(booking);
        }
        
        return result;
    }

    @Override
    @Transactional
    public boolean updateBooking(MeetingBookingRequestVO requestVO) {
        MeetingBooking booking = this.getById(requestVO.getId());
        if (booking == null) {
            throw new RuntimeException("预约记录不存在");
        }
        
        // 只有待审批和已通过的预约可以修改
        if (booking.getStatus() != 0 && booking.getStatus() != 1) {
            throw new RuntimeException("当前状态不允许修改");
        }
        
        // 解析时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(requestVO.getStartTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(requestVO.getEndTime(), formatter);
        
        // 检查时间是否合理
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("开始时间不能晚于结束时间");
        }
        
        // 检查时间冲突
        if (hasTimeConflict(requestVO.getRoomId(), startTime, endTime, requestVO.getId())) {
            throw new RuntimeException("该时间段已被预约");
        }
        
        // 更新预约
        booking.setMeetingTitle(requestVO.getMeetingTitle());
        booking.setRoomId(requestVO.getRoomId());
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setReason(requestVO.getReason());
        booking.setAttendees(requestVO.getAttendees());
        
        // 如果是已通过的预约，修改后需要重新审批
        if (booking.getStatus() == 1) {
            booking.setStatus(0); // 重新设为待审批
            booking.setApproveId(null);
            booking.setApproveTime(null);
            booking.setApproveRemark(null);
            
            // 发送通知给管理员
            sendNotificationToAdmins(booking);
        }
        
        return this.updateById(booking);
    }

    @Override
    @Transactional
    public boolean cancelBooking(Long id, Long bookerId) {
        MeetingBooking booking = this.getById(id);
        if (booking == null) {
            throw new RuntimeException("预约记录不存在");
        }
        
        // 只有预约人可以取消自己的预约
        if (!booking.getBookerId().equals(bookerId)) {
            throw new RuntimeException("无权限取消此预约");
        }
        
        // 只有待审批和已通过的预约可以取消
        if (booking.getStatus() != 0 && booking.getStatus() != 1) {
            throw new RuntimeException("当前状态不允许取消");
        }
        
        // 取消预约
        booking.setStatus(3); // 已取消
        
        boolean result = this.updateById(booking);
        
        if (result && booking.getStatus() == 1) {
            // 如果是已通过的预约被取消，发送通知给管理员
            sendNotificationToAdmins(booking, "预约已取消");
        }
        
        return result;
    }

    @Override
    @Transactional
    public boolean approveBooking(Long id, Long approveId, boolean approved, String remark) {
        MeetingBooking booking = this.getById(id);
        if (booking == null) {
            throw new RuntimeException("预约记录不存在");
        }
        
        // 只有待审批的预约可以审批
        if (booking.getStatus() != 0) {
            throw new RuntimeException("当前状态不允许审批");
        }
        
        // 更新审批信息
        booking.setStatus(approved ? 1 : 2); // 1-已通过，2-已驳回
        booking.setApproveId(approveId);
        booking.setApproveTime(LocalDateTime.now());
        booking.setApproveRemark(remark);
        
        boolean result = this.updateById(booking);
        
        if (result) {
            // 发送审批结果通知给预约人
            sendApprovalNotification(booking, approved);
        }
        
        return result;
    }

    @Override
    public boolean hasTimeConflict(Long roomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        List<MeetingBooking> conflictingBookings = baseMapper.findConflictingBookings(roomId, startTime, endTime);
        
        // 如果是更新操作，排除自己
        if (excludeId != null) {
            conflictingBookings = conflictingBookings.stream()
                    .filter(booking -> !booking.getId().equals(excludeId))
                    .collect(Collectors.toList());
        }
        
        return !conflictingBookings.isEmpty();
    }

    @Override
    public int getUnreadNotificationCount(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_read", 0);
        
        return Math.toIntExact(notificationMapper.selectCount(queryWrapper));
    }

    @Override
    public List<NotificationVO> getUserNotifications(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT 50"); // 限制返回50条
        
        List<Notification> notifications = notificationMapper.selectList(queryWrapper);
        
        return notifications.stream()
                .map(this::convertNotificationToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean markNotificationAsRead(Long notificationId, Long userId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null || !notification.getUserId().equals(userId)) {
            return false;
        }
        
        notification.setIsRead(1);
        notification.setReadTime(LocalDateTime.now());
        
        return notificationMapper.updateById(notification) > 0;
    }

    @Override
    @Transactional
    public boolean checkInMeeting(Long bookingId, Long userId) {
        MeetingBooking booking = this.getById(bookingId);
        if (booking == null) {
            throw new RuntimeException("预约记录不存在");
        }
        
        // 只有已通过的预约可以签到
        if (booking.getStatus() != 1) {
            throw new RuntimeException("当前状态不允许签到");
        }
        
        // 检查是否在会议时间范围内（提前15分钟，延后30分钟）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInStart = booking.getStartTime().minusMinutes(15);
        LocalDateTime checkInEnd = booking.getEndTime().plusMinutes(30);
        
        if (now.isBefore(checkInStart) || now.isAfter(checkInEnd)) {
            throw new RuntimeException("不在签到时间范围内");
        }
        
        // 检查是否已经签到
        QueryWrapper<MeetingCheckin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("booking_id", bookingId);
        queryWrapper.eq("user_id", userId);
        
        MeetingCheckin existingCheckin = meetingCheckinMapper.selectOne(queryWrapper);
        if (existingCheckin != null) {
            throw new RuntimeException("已经签到");
        }
        
        // 创建签到记录
        MeetingCheckin checkin = new MeetingCheckin();
        checkin.setBookingId(bookingId);
        checkin.setUserId(userId);
        checkin.setCheckinTime(now);
        
        return meetingCheckinMapper.insert(checkin) > 0;
    }

    /**
     * 实体转VO
     */
    private MeetingBookingVO convertToVO(MeetingBooking booking) {
        MeetingBookingVO vo = new MeetingBookingVO();
        BeanUtils.copyProperties(booking, vo);
        
        // 设置状态描述
        switch (booking.getStatus()) {
            case 0:
                vo.setStatusDesc("待审批");
                break;
            case 1:
                vo.setStatusDesc("已通过");
                break;
            case 2:
                vo.setStatusDesc("已驳回");
                break;
            case 3:
                vo.setStatusDesc("已取消");
                break;
        }
        
        // 获取会议室名称
        if (booking.getRoomId() != null) {
            MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
            if (room != null) {
                vo.setRoomName(room.getName());
            }
        }
        
        // 获取预约人姓名
        if (booking.getBookerId() != null) {
            User booker = userMapper.selectById(booking.getBookerId());
            if (booker != null) {
                vo.setBookerName(booker.getRealName());
            }
        }
        
        // 获取审批人姓名
        if (booking.getApproveId() != null) {
            User approver = userMapper.selectById(booking.getApproveId());
            if (approver != null) {
                vo.setApproveName(approver.getRealName());
            }
        }
        
        return vo;
    }

    /**
     * 通知实体转VO
     */
    private NotificationVO convertNotificationToVO(Notification notification) {
        NotificationVO vo = new NotificationVO();
        BeanUtils.copyProperties(notification, vo);
        
        // 设置类型描述
        if (notification.getType() != null) {
            switch (notification.getType()) {
                case 0:
                    vo.setTypeDesc("审批结果通知");
                    break;
                case 1:
                    vo.setTypeDesc("会议开始前通知");
                    break;
            }
        }
        
        return vo;
    }

    /**
     * 发送通知给管理员
     */
    private void sendNotificationToAdmins(MeetingBooking booking) {
        sendNotificationToAdmins(booking, "新的预约申请");
    }

    /**
     * 发送通知给管理员
     */
    private void sendNotificationToAdmins(MeetingBooking booking, String title) {
        // 查询所有管理员
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role", 1); // 1-管理员
        queryWrapper.eq("status", 1); // 1-启用
        
        List<User> admins = userMapper.selectList(queryWrapper);
        
        // 获取会议室名称
        MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "未知会议室";
        
        // 获取预约人姓名
        User booker = userMapper.selectById(booking.getBookerId());
        String bookerName = booker != null ? booker.getRealName() : "未知用户";
        
        // 发送通知
        for (User admin : admins) {
            Notification notification = new Notification();
            notification.setUserId(admin.getId());
            notification.setTitle(title);
            notification.setContent(String.format("%s 预约了 %s，会议时间：%s 至 %s，请及时审批。", 
                    bookerName, roomName, 
                    booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    booking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            notification.setType(0); // 审批结果通知
            notification.setRelatedId(booking.getId());
            notification.setIsRead(0);
            
            notificationMapper.insert(notification);
        }
    }

    /**
     * 发送审批结果通知给预约人
     */
    private void sendApprovalNotification(MeetingBooking booking, boolean approved) {
        // 获取会议室名称
        MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "未知会议室";
        
        Notification notification = new Notification();
        notification.setUserId(booking.getBookerId());
        notification.setTitle(approved ? "预约已通过" : "预约已驳回");
        notification.setContent(String.format("您预约的 %s %s，会议时间：%s 至 %s。%s", 
                roomName, 
                approved ? "已通过审批" : "已被驳回",
                booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                approved ? "" : "驳回原因：" + (booking.getApproveRemark() != null ? booking.getApproveRemark() : "无")));
        notification.setType(0); // 审批结果通知
        notification.setRelatedId(booking.getId());
        notification.setIsRead(0);
        
        notificationMapper.insert(notification);
    }
}