package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.LogTag;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.ActivityCreateRequest;
import com.rosy.main.domain.dto.ActivityQueryRequest;
import com.rosy.main.domain.dto.ActivityUpdateRequest;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.service.IActivityService;
import com.rosy.web.controller.main.utils.UserHolder;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动管理控制器
 */
@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Autowired
    private IActivityService activityService;

    /**
     * 创建活动
     */
    @PostMapping("/create")
    @ValidateRequest
    @LogTag(value = "创建活动", printResult = true)
    public ApiResponse createActivity(@Valid @RequestBody ActivityCreateRequest request) {
        Long organizerId = UserHolder.getUserId();
        Long activityId = activityService.createActivity(request, organizerId);
        return ApiResponse.success(activityId);
    }

    /**
     * 更新活动
     */
    @PostMapping("/update")
    @ValidateRequest
    @LogTag(value = "更新活动", printResult = true)
    public ApiResponse updateActivity(@Valid @RequestBody ActivityUpdateRequest request) {
        boolean result = activityService.updateActivity(request);
        return ApiResponse.success(result);
    }

    /**
     * 删除活动
     */
    @PostMapping("/delete")
    @ValidateRequest
    @LogTag(value = "删除活动", printResult = true)
    public ApiResponse deleteActivity(@Valid @RequestBody IdRequest request) {
        boolean result = activityService.deleteActivity(request.getId());
        return ApiResponse.success(result);
    }

    /**
     * 根据ID获取活动详情
     */
    @GetMapping("/get/{id}")
    @LogTag(value = "获取活动详情")
    public ApiResponse getActivityById(@PathVariable("id") Long id) {
        ActivityVO activityVO = activityService.getActivityById(id);
        // 增加浏览次数
        activityService.incrementViewCount(id);
        return ApiResponse.success(activityVO);
    }

    /**
     * 分页查询活动列表
     */
    @PostMapping("/list/page")
    @LogTag(value = "分页查询活动列表")
    public ApiResponse listActivitiesByPage(@RequestBody ActivityQueryRequest request) {
        Page<ActivityVO> page = activityService.listActivities(request);
        return ApiResponse.success(page);
    }

    /**
     * 获取活动列表（不分页）
     */
    @PostMapping("/list")
    @LogTag(value = "查询活动列表")
    public ApiResponse listActivities(@RequestBody ActivityQueryRequest request) {
        List<ActivityVO> list = activityService.listAllActivities(request);
        return ApiResponse.success(list);
    }

    /**
     * 取消活动
     */
    @PostMapping("/cancel/{id}")
    @ValidateRequest
    @LogTag(value = "取消活动", printResult = true)
    public ApiResponse cancelActivity(@PathVariable("id") Long id) {
        boolean result = activityService.cancelActivity(id);
        return ApiResponse.success(result);
    }
}
