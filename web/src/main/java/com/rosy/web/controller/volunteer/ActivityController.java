package com.rosy.web.controller.volunteer;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.activity.ActivityAddRequest;
import com.rosy.main.domain.dto.activity.ActivityQueryRequest;
import com.rosy.main.domain.dto.activity.ActivityUpdateRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.service.IActivityService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addActivity(@RequestBody ActivityAddRequest addRequest) {
        Activity activity = BeanUtil.copyProperties(addRequest, Activity.class);
        activity.setStatus((byte) 1);
        activity.setCurrentCount(0);
        boolean result = activityService.save(activity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(activity.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteActivity(@RequestBody IdRequest idRequest) {
        boolean result = activityService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateActivity(@RequestBody ActivityUpdateRequest updateRequest) {
        if (updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Activity activity = BeanUtil.copyProperties(updateRequest, Activity.class);
        boolean result = activityService.updateById(activity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getActivityById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Activity activity = activityService.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(activity);
    }

    @GetMapping("/get/vo")
    public ApiResponse getActivityVOById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Activity activity = activityService.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(activityService.getActivityVO(activity));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listActivityByPage(@RequestBody ActivityQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<Activity> activityPage = activityService.page(
                new Page<>(current, size),
                activityService.getQueryWrapper(queryRequest));
        return ApiResponse.success(activityPage);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listActivityVOByPage(@RequestBody ActivityQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Activity> activityPage = activityService.page(
                new Page<>(current, size),
                activityService.getQueryWrapper(queryRequest));
        Page<ActivityVO> activityVOPage = PageUtils.convert(activityPage, activityService::getActivityVO);
        return ApiResponse.success(activityVOPage);
    }
}
