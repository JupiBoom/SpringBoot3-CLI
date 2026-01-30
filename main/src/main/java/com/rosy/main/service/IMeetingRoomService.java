package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;

/**
 * 会议室Service接口
 */
public interface IMeetingRoomService extends IService<MeetingRoom> {

    /**
     * 分页查询会议室
     * @param page 分页参数
     * @param name 会议室名称（可选）
     * @param location 会议室位置（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    Page<MeetingRoomVO> getMeetingRoomPage(Page<MeetingRoom> page, String name, String location, Integer status);

    /**
     * 根据ID获取会议室详情
     * @param id 会议室ID
     * @return 会议室详情
     */
    MeetingRoomVO getMeetingRoomById(Long id);

    /**
     * 添加会议室
     * @param meetingRoom 会议室信息
     * @return 是否成功
     */
    boolean addMeetingRoom(MeetingRoom meetingRoom);

    /**
     * 更新会议室
     * @param meetingRoom 会议室信息
     * @return 是否成功
     */
    boolean updateMeetingRoom(MeetingRoom meetingRoom);

    /**
     * 删除会议室
     * @param id 会议室ID
     * @return 是否成功
     */
    boolean deleteMeetingRoom(Long id);

    /**
     * 获取所有可用的会议室
     * @return 会议室列表
     */
    java.util.List<MeetingRoomVO> getAvailableRooms();
}