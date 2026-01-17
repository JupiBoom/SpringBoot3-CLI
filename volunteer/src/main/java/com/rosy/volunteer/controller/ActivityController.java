package com.rosy.volunteer.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.dto.ActivityCreateDTO;
import com.rosy.volunteer.domain.dto.ActivityUpdateDTO;
import com.rosy.volunteer.domain.vo.ActivityDetailVO;
import com.rosy.volunteer.domain.vo.ActivityListVO;
import com.rosy.volunteer.service.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "活动管理")
@RestController
@RequestMapping("/api/volunteer/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final IActivityService activityService;

    @Operation(summary = "创建活动")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ApiResponse<Long> createActivity(@Valid @RequestBody ActivityCreateDTO dto) {
        Long id = activityService.createActivity(dto);
        return ApiResponse.success(id);
    }

    @Operation(summary = "更新活动")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ApiResponse<Void> updateActivity(@PathVariable Long id, @Valid @RequestBody ActivityUpdateDTO dto) {
        activityService.updateActivity(id, dto);
        return ApiResponse.success();
    }

    @Operation(summary = "删除活动")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ApiResponse<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ApiResponse.success();
    }

    @Operation(summary = "获取活动详情")
    @GetMapping("/{id}")
    public ApiResponse<ActivityDetailVO> getActivityDetail(@PathVariable Long id) {
        ActivityDetailVO vo = activityService.getActivityDetail(id);
        return ApiResponse.success(vo);
    }

    @Operation(summary = "获取活动列表")
    @GetMapping("/list")
    public ApiResponse<List<ActivityListVO>> getActivityList(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        List<ActivityListVO> list = activityService.getActivityList(categoryId, status, keyword);
        return ApiResponse.success(list);
    }

    @Operation(summary = "分页获取活动列表")
    @GetMapping("/page")
    public ApiResponse<Object> getActivityPage(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @Valid PageRequest pageRequest) {
        return ApiResponse.success(activityService.getActivityPage(categoryId, status, keyword, pageRequest));
    }

    @Operation(summary = "更新活动状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ApiResponse<Void> updateActivityStatus(@PathVariable Long id, @RequestParam Integer status) {
        activityService.updateActivityStatus(id, status);
        return ApiResponse.success();
    }

    @Operation(summary = "获取热门活动")
    @GetMapping("/hot")
    public ApiResponse<List<ActivityListVO>> getHotActivities(@RequestParam(defaultValue = "6") Integer limit) {
        List<ActivityListVO> list = activityService.getHotActivities(limit);
        return ApiResponse.success(list);
    }

    @Operation(summary = "获取即将开始的活动")
    @GetMapping("/upcoming")
    public ApiResponse<List<ActivityListVO>> getUpcomingActivities(@RequestParam(defaultValue = "6") Integer limit) {
        List<ActivityListVO> list = activityService.getUpcomingActivities(limit);
        return ApiResponse.success(list);
    }
}
