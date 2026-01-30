package com.rosy.web.controller.meeting;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomAddRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomQueryRequest;
import com.rosy.main.domain.dto.meetingroom.MeetingRoomUpdateRequest;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.vo.MeetingRoomVO;
import com.rosy.main.service.IMeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meeting-room")
@Tag(name = "会议室管理", description = "会议室的增删改查")
public class MeetingRoomController {

    @Resource
    private IMeetingRoomService meetingRoomService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "添加会议室")
    public ApiResponse addMeetingRoom(@RequestBody MeetingRoomAddRequest request) {
        MeetingRoom meetingRoom = new MeetingRoom();
        BeanUtils.copyProperties(request, meetingRoom);
        meetingRoom.setStatus((byte) 1);
        boolean result = meetingRoomService.save(meetingRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(meetingRoom.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除会议室")
    public ApiResponse deleteMeetingRoom(@RequestBody IdRequest idRequest) {
        boolean result = meetingRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新会议室")
    public ApiResponse updateMeetingRoom(@RequestBody MeetingRoomUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom meetingRoom = BeanUtil.copyProperties(request, MeetingRoom.class);
        boolean result = meetingRoomService.updateById(meetingRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取会议室详情")
    public ApiResponse getMeetingRoomById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom meetingRoom = meetingRoomService.getById(id);
        ThrowUtils.throwIf(meetingRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingRoom);
    }

    @GetMapping("/get/vo")
    @Operation(summary = "获取会议室VO")
    public ApiResponse getMeetingRoomVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        MeetingRoom meetingRoom = meetingRoomService.getById(id);
        ThrowUtils.throwIf(meetingRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(meetingRoomService.getMeetingRoomVO(meetingRoom));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取会议室列表")
    public ApiResponse listMeetingRoomByPage(@RequestBody MeetingRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<MeetingRoom> page = meetingRoomService.page(new Page<>(current, size),
                meetingRoomService.getQueryWrapper(queryRequest));
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    @Operation(summary = "分页获取会议室VO列表")
    public ApiResponse listMeetingRoomVOByPage(@RequestBody MeetingRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<MeetingRoom> page = meetingRoomService.page(new Page<>(current, size),
                meetingRoomService.getQueryWrapper(queryRequest));
        Page<MeetingRoomVO> voPage = PageUtils.convert(page, meetingRoomService::getMeetingRoomVO);
        return ApiResponse.success(voPage);
    }

    @GetMapping("/list/available")
    @Operation(summary = "获取可用会议室列表")
    public ApiResponse listAvailableRooms() {
        return ApiResponse.success(meetingRoomService.lambdaQuery()
                .eq(MeetingRoom::getStatus, 1)
                .list());
    }
}
