package com.rosy.web.controller.main;

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
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addActivity(@RequestBody ActivityAddRequest activityAddRequest) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityAddRequest, activity);
        activity.setStatus((byte) 0);
        activity.setCurrentPeople(0);
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
    public ApiResponse updateActivity(@RequestBody ActivityUpdateRequest activityUpdateRequest) {
        if (activityUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Activity activity = BeanUtil.copyProperties(activityUpdateRequest, Activity.class);
        boolean result = activityService.updateById(activity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getActivityById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Activity activity = activityService.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(activity);
    }

    @GetMapping("/get/vo")
    public ApiResponse getActivityVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Activity activity = activityService.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(activityService.getActivityVO(activity));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listActivityByPage(@RequestBody ActivityQueryRequest activityQueryRequest) {
        long current = activityQueryRequest.getCurrent();
        long size = activityQueryRequest.getPageSize();
        Page<Activity> activityPage = activityService.page(new Page<>(current, size), activityService.getQueryWrapper(activityQueryRequest));
        return ApiResponse.success(activityPage);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listActivityVOByPage(@RequestBody ActivityQueryRequest activityQueryRequest) {
        long current = activityQueryRequest.getCurrent();
        long size = activityQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Activity> activityPage = activityService.page(new Page<>(current, size), activityService.getQueryWrapper(activityQueryRequest));
        Page<ActivityVO> activityVOPage = PageUtils.convert(activityPage, activityService::getActivityVO);
        return ApiResponse.success(activityVOPage);
    }

    @PostMapping("/status/update")
    @ValidateRequest
    public ApiResponse updateActivityStatus(@RequestBody IdRequest idRequest, @RequestParam byte status) {
        boolean result = activityService.updateActivityStatus(idRequest.getId(), status);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }
}
