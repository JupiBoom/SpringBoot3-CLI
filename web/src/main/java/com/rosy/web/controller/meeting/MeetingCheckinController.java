package com.rosy.web.controller.meeting;

import com.rosy.common.annotation.LogTag;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.MeetingCheckinRequest;
import com.rosy.main.domain.vo.MeetingCheckinVO;
import com.rosy.main.service.IMeetingCheckinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "会议签到管理")
@RestController
@RequestMapping("/api/meeting/checkin")
public class MeetingCheckinController {

    @Resource
    private IMeetingCheckinService meetingCheckinService;

    @Operation(summary = "会议签到")
    @PostMapping("/checkin")
    @LogTag
    public ApiResponse checkin(@RequestBody MeetingCheckinRequest request) {
        Boolean result = meetingCheckinService.checkin(request);
        return ApiResponse.success(result);
    }

    @Operation(summary = "获取会议签到记录")
    @GetMapping("/reservation/{reservationId}")
    public ApiResponse getCheckinsByReservation(@PathVariable Long reservationId) {
        List<MeetingCheckinVO> checkins = meetingCheckinService.getCheckinsByReservation(reservationId);
        return ApiResponse.success(checkins);
    }

    @Operation(summary = "获取我的签到记录")
    @GetMapping("/my")
    public ApiResponse getMyCheckins() {
        List<MeetingCheckinVO> checkins = meetingCheckinService.getCheckinsByUser(1L);
        return ApiResponse.success(checkins);
    }
}
