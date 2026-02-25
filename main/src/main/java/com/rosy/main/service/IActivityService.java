package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.ActivityDTO;
import com.rosy.main.domain.dto.ActivityQueryDTO;
import com.rosy.main.domain.entity.Activity;

public interface IActivityService extends IService<Activity> {
    Activity create(ActivityDTO dto, Long creatorId);
    Activity update(Long id, ActivityDTO dto);
    boolean delete(Long id);
    Page<Activity> pageQuery(ActivityQueryDTO queryDTO);
    boolean updateStatus(Long id, String status);
}
