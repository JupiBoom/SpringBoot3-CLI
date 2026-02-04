package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ActivityCreateRequest;
import com.rosy.main.domain.dto.ActivityQueryRequest;
import com.rosy.main.domain.dto.ActivityUpdateRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.vo.ActivityVO;

import java.util.List;

/**
 * 活动服务接口
 */
public interface IActivityService extends IService<Activity> {

    /**
     * 创建活动
     */
    Long createActivity(ActivityCreateRequest request, Long organizerId);

    /**
     * 更新活动
     */
    boolean updateActivity(ActivityUpdateRequest request);

    /**
     * 删除活动
     */
    boolean deleteActivity(Long id);

    /**
     * 根据ID获取活动
     */
    ActivityVO getActivityById(Long id);

    /**
     * 分页查询活动列表
     */
    Page<ActivityVO> listActivities(ActivityQueryRequest request);

    /**
     * 获取活动列表（不分页）
     */
    List<ActivityVO> listAllActivities(ActivityQueryRequest request);

    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);

    /**
     * 取消活动
     */
    boolean cancelActivity(Long id);

    /**
     * 获取查询条件
     */
    QueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest request);
}
