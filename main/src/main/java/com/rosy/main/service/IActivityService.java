package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Activity;

/**
 * 活动Service接口
 */
public interface IActivityService extends IService<Activity> {

    /**
     * 分页查询活动
     *
     * @param page        分页参数
     * @param title       活动标题（可选）
     * @param category    活动分类（可选）
     * @param status      活动状态（可选）
     * @param location    活动地点（可选）
     * @param creatorId   创建者ID（可选）
     * @return 活动分页数据
     */
    Page<Activity> getActivityPage(Page<Activity> page, String title, Integer category, 
                                 Integer status, String location, Long creatorId);

    /**
     * 更新活动状态
     *
     * @param activityId 活动ID
     * @param status     新状态
     * @return 是否成功
     */
    boolean updateActivityStatus(Long activityId, Integer status);

    /**
     * 增加活动当前报名人数
     *
     * @param activityId 活动ID
     * @return 是否成功
     */
    boolean increaseCurrentCount(Long activityId);

    /**
     * 减少活动当前报名人数
     *
     * @param activityId 活动ID
     * @return 是否成功
     */
    boolean decreaseCurrentCount(Long activityId);
}