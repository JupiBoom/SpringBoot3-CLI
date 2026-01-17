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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 活动管理前端控制器
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Tag(name = "活动管理", description = "活动的增删改查接口")
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private IActivityService activityService;

    /**
     * 创建活动
     */
    @Operation(summary = "创建活动")
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse<Long> addActivity(@RequestBody ActivityAddRequest activityAddRequest) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityAddRequest, activity);
        // 初始化已报名人数为0
        activity.setRegisteredPeople(0);
        // 初始状态为招募中
        activity.setStatus((byte) 1);
        boolean result = activityService.save(activity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(activity.getId());
    }

    /**
     * 删除活动
     */
    @Operation(summary = "删除活动")
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse<Boolean> deleteActivity(@RequestBody IdRequest idRequest) {
        boolean result = activityService.removeById(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 更新活动
     */
    @Operation(summary = "更新活动")
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse<Boolean> updateActivity(@RequestBody ActivityUpdateRequest activityUpdateRequest) {
        if (activityUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "活动ID不能为空");
        }
        Activity activity = BeanUtil.copyProperties(activityUpdateRequest, Activity.class);
        boolean result = activityService.updateById(activity);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取活动详情
     */
    @Operation(summary = "根据ID获取活动详情")
    @GetMapping("/get")
    public ApiResponse<Activity> getActivityById(@RequestParam Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "活动ID不合法");
        }
        Activity activity = activityService.getById(id);
        ThrowUtils.throwIf(activity == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(activity);
    }

    /**
     * 根据ID获取活动详情(VO)
     */
    @Operation(summary = "根据ID获取活动详情(VO)")
    @GetMapping("/get/vo")
    public ApiResponse<ActivityVO> getActivityVOById(@RequestParam Long id) {
        ApiResponse<Activity> response = getActivityById(id);
        Activity activity = response.getData();
        ActivityVO activityVO = activityService.getActivityVO(activity);
        // 设置分类和状态名称
        setCategoryAndStatusName(activityVO);
        // 计算报名百分比
        if (activityVO.getRequiredPeople() != null && activityVO.getRequiredPeople() > 0) {
            activityVO.setRegistrationPercentage(
                    (double) activityVO.getRegisteredPeople() / activityVO.getRequiredPeople() * 100
            );
        }
        return ApiResponse.success(activityVO);
    }

    /**
     * 分页获取活动列表
     */
    @Operation(summary = "分页获取活动列表")
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse<Page<Activity>> listActivityByPage(@RequestBody ActivityQueryRequest activityQueryRequest) {
        long current = activityQueryRequest.getCurrent();
        long size = activityQueryRequest.getPageSize();
        Page<Activity> activityPage = activityService.page(
                new Page<>(current, size),
                activityService.getQueryWrapper(activityQueryRequest)
        );
        return ApiResponse.success(activityPage);
    }

    /**
     * 分页获取活动列表(VO)
     */
    @Operation(summary = "分页获取活动列表(VO)")
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse<Page<ActivityVO>> listActivityVOByPage(@RequestBody ActivityQueryRequest activityQueryRequest) {
        long current = activityQueryRequest.getCurrent();
        long size = activityQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 50, ErrorCode.PARAMS_ERROR, "每页条数不能超过50");
        Page<Activity> activityPage = activityService.page(
                new Page<>(current, size),
                activityService.getQueryWrapper(activityQueryRequest)
        );
        Page<ActivityVO> activityVOPage = PageUtils.convert(activityPage, activityService::getActivityVO);
        // 设置分类和状态名称
        activityVOPage.getRecords().forEach(this::setCategoryAndStatusName);
        return ApiResponse.success(activityVOPage);
    }

    /**
     * 设置分类和状态名称
     */
    private void setCategoryAndStatusName(ActivityVO activityVO) {
        // 设置分类名称
        if (activityVO.getCategory() != null) {
            switch (activityVO.getCategory()) {
                case 1 -> activityVO.setCategoryName("环保");
                case 2 -> activityVO.setCategoryName("助老");
                case 3 -> activityVO.setCategoryName("教育");
                case 4 -> activityVO.setCategoryName("医疗");
            }
        }
        // 设置状态名称
        if (activityVO.getStatus() != null) {
            switch (activityVO.getStatus()) {
                case 1 -> activityVO.setStatusName("招募中");
                case 2 -> activityVO.setStatusName("进行中");
                case 3 -> activityVO.setStatusName("已完成");
            }
        }
    }
}