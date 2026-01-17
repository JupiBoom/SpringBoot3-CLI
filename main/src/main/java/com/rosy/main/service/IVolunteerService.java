package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Volunteer;
import com.rosy.main.domain.vo.VolunteerVO;
import com.rosy.main.dto.req.VolunteerAddRequest;
import com.rosy.main.dto.req.VolunteerUpdateRequest;
import com.rosy.main.dto.req.VolunteerQueryRequest;

import java.util.List;

public interface IVolunteerService extends IService<Volunteer> {

    /**
     * 添加志愿者
     */
    Long addVolunteer(VolunteerAddRequest request);

    /**
     * 更新志愿者信息
     */
    void updateVolunteer(VolunteerUpdateRequest request);

    /**
     * 删除志愿者
     */
    void deleteVolunteer(Long id);

    /**
     * 根据ID获取志愿者VO
     */
    VolunteerVO getVolunteerVO(Long id);

    /**
     * 分页查询志愿者
     */
    List<VolunteerVO> listVolunteerVO(VolunteerQueryRequest request);

    /**
     * 根据手机号查询志愿者
     */
    VolunteerVO getVolunteerByPhone(String phone);

    /**
     * 更新志愿者服务时长
     */
    void updateVolunteerServiceDuration(Long volunteerId, String serviceDuration);

    /**
     * 获取志愿者排行榜
     */
    List<VolunteerVO> getVolunteerRanking(int limit);
}