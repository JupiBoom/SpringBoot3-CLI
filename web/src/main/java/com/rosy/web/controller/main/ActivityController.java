package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.ActivityDTO;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.service.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "活动管理")
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final IActivityService activityService;

    @Operation(summary = "创建活动")
    @PostMapping
    public ApiResponse createActivity(@Validated @RequestBody ActivityDTO dto) {
        Long userId = 1L;
        return ApiResponse.success(activityService.createActivity(dto, userId));
    }

    @Operation(summary = "更新活动")
    @PutMapping("/{activityId}")
    public ApiResponse updateActivity(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Validated @RequestBody ActivityDTO dto) {
        activityService.updateActivity(dto, activityId);
        return ApiResponse.success();
    }

    @Operation(summary = "删除活动")
    @DeleteMapping("/{activityId}")
    public ApiResponse deleteActivity(
            @Parameter(description = "活动ID") @PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ApiResponse.success();
    }

    @Operation(summary = "获取活动详情")
    @GetMapping("/{activityId}")
    public ApiResponse getActivityDetail(
            @Parameter(description = "活动ID") @PathVariable Long activityId) {
        return ApiResponse.success(activityService.getActivityDetail(activityId));
    }

    @Operation(summary = "分页查询活动")
    @GetMapping("/page")
    public ApiResponse getActivityPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "活动分类") @RequestParam(required = false) Integer category,
            @Parameter(description = "活动状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        return ApiResponse.success(activityService.getActivityPage(pageNum, pageSize, category, status, keyword));
    }
}
