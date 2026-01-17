package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Volunteer;
import com.rosy.main.domain.vo.VolunteerVO;
import com.rosy.main.dto.req.VolunteerAddRequest;
import com.rosy.main.dto.req.VolunteerUpdateRequest;
import com.rosy.main.dto.req.VolunteerQueryRequest;
import com.rosy.main.mapper.VolunteerMapper;
import com.rosy.main.service.IVolunteerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl extends ServiceImpl<VolunteerMapper, Volunteer> implements IVolunteerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addVolunteer(VolunteerAddRequest request) {
        // 检查手机号是否已存在
        LambdaQueryWrapper<Volunteer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Volunteer::getPhone, request.getPhone());
        if (count(wrapper) > 0) {
            throw new RuntimeException("该手机号已注册");
        }
        Volunteer volunteer = new Volunteer();
        BeanUtil.copyProperties(request, volunteer);
        volunteer.setTotalServiceDuration(0L);
        volunteer.setServiceCount(0);
        volunteer.setStatus(1);
        volunteer.setCreateTime(LocalDateTime.now());
        save(volunteer);
        return volunteer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVolunteer(VolunteerUpdateRequest request) {
        Volunteer volunteer = getById(request.getId());
        if (volunteer == null) {
            throw new RuntimeException("志愿者不存在");
        }
        BeanUtil.copyProperties(request, volunteer, "id", "phone", "createTime");
        volunteer.setUpdateTime(LocalDateTime.now());
        updateById(volunteer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVolunteer(Long id) {
        removeById(id);
    }

    @Override
    public VolunteerVO getVolunteerVO(Long id) {
        Volunteer volunteer = getById(id);
        if (volunteer == null) {
            return null;
        }
        return toVO(volunteer);
    }

    @Override
    public List<VolunteerVO> listVolunteerVO(VolunteerQueryRequest request) {
        LambdaQueryWrapper<Volunteer> wrapper = getQueryWrapper(request);
        Page<Volunteer> page = new Page<>(request.getPageNum(), request.getPageSize());
        page(page, wrapper);
        return page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public VolunteerVO getVolunteerByPhone(String phone) {
        LambdaQueryWrapper<Volunteer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Volunteer::getPhone, phone);
        Volunteer volunteer = getOne(wrapper);
        if (volunteer == null) {
            return null;
        }
        return toVO(volunteer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVolunteerServiceDuration(Long volunteerId, String serviceDuration) {
        Volunteer volunteer = getById(volunteerId);
        if (volunteer == null) {
            throw new RuntimeException("志愿者不存在");
        }
        try {
            String[] parts = serviceDuration.split("小时");
            if (parts.length > 0) {
                long hours = Long.parseLong(parts[0].trim());
                volunteer.setTotalServiceDuration(volunteer.getTotalServiceDuration() + hours);
                volunteer.setServiceCount(volunteer.getServiceCount() + 1);
                updateById(volunteer);
            }
        } catch (NumberFormatException e) {
            log.warn("解析时长失败: {}", serviceDuration);
        }
    }

    @Override
    public List<VolunteerVO> getVolunteerRanking(int limit) {
        LambdaQueryWrapper<Volunteer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Volunteer::getStatus, 1)
               .orderByDesc(Volunteer::getTotalServiceDuration)
               .last("LIMIT " + limit);
        List<Volunteer> volunteers = list(wrapper);
        return volunteers.stream()
                        .map(this::toVO)
                        .collect(Collectors.toList());
    }

    private LambdaQueryWrapper<Volunteer> getQueryWrapper(VolunteerQueryRequest request) {
        LambdaQueryWrapper<Volunteer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Volunteer::getStatus, 1);
        if (StrUtil.isNotBlank(request.getName())) {
            wrapper.like(Volunteer::getName, request.getName());
        }
        if (StrUtil.isNotBlank(request.getPhone())) {
            wrapper.like(Volunteer::getPhone, request.getPhone());
        }
        if (request.getGender() != null) {
            wrapper.eq(Volunteer::getGender, request.getGender());
        }
        if (request.getMinAge() != null) {
            wrapper.ge(Volunteer::getAge, request.getMinAge());
        }
        if (request.getMaxAge() != null) {
            wrapper.le(Volunteer::getAge, request.getMaxAge());
        }
        wrapper.orderByDesc(Volunteer::getCreateTime);
        return wrapper;
    }

    private VolunteerVO toVO(Volunteer volunteer) {
        VolunteerVO vo = new VolunteerVO();
        BeanUtil.copyProperties(volunteer, vo);
        return vo;
    }
}