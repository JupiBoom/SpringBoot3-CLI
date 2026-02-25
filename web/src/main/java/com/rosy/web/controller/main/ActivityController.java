package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.ActivityDTO;
import com.rosy.main.domain.dto.ActivityQueryDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.service.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
@Tag(name = "活动管理")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    @PostMapping("/add")
    @ValidateRequest
    @Operation(summary = "创建活动")
    public ApiResponse addActivity(@RequestBody ActivityDTO dto) {
        Activity activity = activityService.create(dto, 1L);
        return ApiResponse.success(activity.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除活动")
    public ApiResponse deleteActivity(@RequestBody IdRequest idRequest) {
        boolean result = activityService.delete(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新活动")
    public ApiResponse updateActivity(@RequestParam Long id, @RequestBody ActivityDTO dto) {
        Activity activity = activityService.update(id, dto);
        return ApiResponse.success(activity);
    }

    @GetMapping("/get")
    @Operation(summary = "获取活动详情")
    public ApiResponse getActivity(Long id) {
        if (id == null || id <= 0) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR.getCode(), "参数错误");
        }
        Activity activity = activityService.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(activity);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    @Operation(summary = "分页获取活动列表")
    public ApiResponse listActivityByPage(@RequestBody ActivityQueryDTO queryDTO) {
        Page<Activity> page = activityService.pageQuery(queryDTO);
        return ApiResponse.success(page);
    }

    @PostMapping("/status/update")
    @Operation(summary = "更新活动状态")
    public ApiResponse updateStatus(@RequestBody StatusUpdateRequest request) {
        boolean result = activityService.updateStatus(request.getId(), request.getStatus());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @Data
    public static class StatusUpdateRequest {
        private Long id;
        private String status;
    }
}
