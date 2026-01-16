package com.rosy.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.meeting.domain.entity.MeetingRoom;
import com.rosy.meeting.domain.vo.MeetingRoomCreateVO;
import com.rosy.meeting.domain.vo.MeetingRoomUpdateVO;
import com.rosy.meeting.domain.vo.MeetingRoomVO;

import java.util.List;

/**
 * 会议室服务接口
 */
public interface IMeetingRoomService extends IService<MeetingRoom> {

    /**
     * 创建会议室
     * @param vo 会议室创建请求
     * @return 会议室VO
     */
    MeetingRoomVO createMeetingRoom(MeetingRoomCreateVO vo);

    /**
     * 更新会议室
     * @param vo 会议室更新请求
     * @return 会议室VO
     */
    MeetingRoomVO updateMeetingRoom(MeetingRoomUpdateVO vo);

    /**
     * 删除会议室
     * @param id 会议室ID
     */
    void deleteMeetingRoom(Long id);

    /**
     * 获取会议室详情
     * @param id 会议室ID
     * @return 会议室VO
     */
    MeetingRoomVO getMeetingRoomById(Long id);

    /**
     * 获取所有会议室列表
     * @return 会议室VO列表
     */
    List<MeetingRoomVO> getAllMeetingRooms();

    /**
     * 根据条件查询会议室
     * @param name 会议室名称（可选）
     * @param location 位置（可选）
     * @param status 状态（可选）
     * @return 会议室VO列表
     */
    List<MeetingRoomVO> searchMeetingRooms(String name, String location, Integer status);
}