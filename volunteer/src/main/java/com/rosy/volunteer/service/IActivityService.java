package com.rosy.volunteer.service;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.dto.ActivityCreateDTO;
import com.rosy.volunteer.domain.dto.ActivityUpdateDTO;
import com.rosy.volunteer.domain.vo.ActivityDetailVO;
import com.rosy.volunteer.domain.vo.ActivityListVO;

import java.util.List;

public interface IActivityService {
    Long createActivity(ActivityCreateDTO dto);
    void updateActivity(Long id, ActivityUpdateDTO dto);
    void deleteActivity(Long id);
    ActivityDetailVO getActivityDetail(Long id);
    List<ActivityListVO> getActivityList(Long categoryId, Integer status, String keyword);
    Object getActivityPage(Long categoryId, Integer status, String keyword, PageRequest pageRequest);
    void updateActivityStatus(Long id, Integer status);
    List<ActivityListVO> getHotActivities(Integer limit);
    List<ActivityListVO> getUpcomingActivities(Integer limit);
}
