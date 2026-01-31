package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.enums.ActivityCategoryEnum;
import com.rosy.main.enums.ActivityStatusEnum;
import com.rosy.main.service.IActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动管理控制器
 */
@Tag(name = "活动管理", description = "活动相关接口")
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    @Operation(summary = "创建活动")
    @PostMapping
    public ApiResponse<Long> createActivity(@RequestBody Activity activity) {
        // 设置初始状态为招募中
        activity.setStatus(ActivityStatusEnum.RECRUITING.getCode());
        // 设置初始报名人数为0
        activity.setCurrentCount(0);
        boolean result = activityService.save(activity);
        if (result) {
            return ApiResponse.success(activity.getId());
        } else {
            return ApiResponse.error("创建活动失败");
        }
    }

    @Operation(summary = "根据ID获取活动详情")
    @GetMapping("/{id}")
    public ApiResponse<Activity> getActivityById(@Parameter(description = "活动ID") @PathVariable Long id) {
        Activity activity = activityService.getById(id);
        if (activity != null) {
            return ApiResponse.success(activity);
        } else {
            return ApiResponse.error("活动不存在");
        }
    }

    @Operation(summary = "分页查询活动")
    @GetMapping("/page")
    public ApiResponse<Page<Activity>> getActivityPage(PageRequest pageRequest,
                                                      @Parameter(description = "活动标题") @RequestParam(required = false) String title,
                                                      @Parameter(description = "活动分类") @RequestParam(required = false) Integer category,
                                                      @Parameter(description = "活动状态") @RequestParam(required = false) Integer status,
                                                      @Parameter(description = "活动地点") @RequestParam(required = false) String location,
                                                      @Parameter(description = "创建者ID") @RequestParam(required = false) Long creatorId) {
        Page<Activity> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<Activity> result = activityService.getActivityPage(page, title, category, status, location, creatorId);
        return ApiResponse.success(result);
    }

    @Operation(summary = "更新活动")
    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateActivity(@Parameter(description = "活动ID") @PathVariable Long id,
                                              @RequestBody Activity activity) {
        activity.setId(id);
        boolean result = activityService.updateById(activity);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("更新活动失败");
        }
    }

    @Operation(summary = "删除活动")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteActivity(@Parameter(description = "活动ID") @PathVariable Long id) {
        boolean result = activityService.removeById(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("删除活动失败");
        }
    }

    @Operation(summary = "批量删除活动")
    @DeleteMapping("/batch")
    public ApiResponse<Boolean> deleteActivityBatch(@RequestBody List<Long> ids) {
        boolean result = activityService.removeByIds(ids);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("批量删除活动失败");
        }
    }

    @Operation(summary = "更新活动状态")
    @PutMapping("/{id}/status")
    public ApiResponse<Boolean> updateActivityStatus(@Parameter(description = "活动ID") @PathVariable Long id,
                                                    @Parameter(description = "状态") @RequestParam Integer status) {
        // 验证状态值是否合法
        ActivityStatusEnum statusEnum = ActivityStatusEnum.getByCode(status);
        if (statusEnum == null) {
            return ApiResponse.error("无效的状态值");
        }
        
        boolean result = activityService.updateActivityStatus(id, status);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("更新活动状态失败");
        }
    }

    @Operation(summary = "获取活动分类列表")
    @GetMapping("/categories")
    public ApiResponse<List<ActivityCategoryEnum>> getActivityCategories() {
        return ApiResponse.success(List.of(ActivityCategoryEnum.values()));
    }

    @Operation(summary = "获取活动状态列表")
    @GetMapping("/statuses")
    public ApiResponse<List<ActivityStatusEnum>> getActivityStatuses() {
        return ApiResponse.success(List.of(ActivityStatusEnum.values()));
    }
}