package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ActivityDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.vo.ActivityVO;

public interface IActivityService extends IService<Activity> {

    Long createActivity(ActivityDTO dto, Long userId);

    void updateActivity(ActivityDTO dto, Long userId);

    void deleteActivity(Long id);

    ActivityVO getActivityDetail(Long id);

    Page<ActivityVO> getActivityPage(Integer page, Integer size, Integer category, Integer status, String keyword);

    void updateActivityStatus();
}
