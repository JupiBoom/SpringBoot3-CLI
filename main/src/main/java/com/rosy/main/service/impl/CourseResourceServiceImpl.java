package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.utils.SqlUtils;
import com.rosy.main.domain.dto.resource.CourseResourceQueryRequest;
import com.rosy.main.domain.entity.CourseResource;
import com.rosy.main.mapper.CourseResourceMapper;
import com.rosy.main.service.ICourseResourceService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseResourceServiceImpl extends ServiceImpl<CourseResourceMapper, CourseResource>
        implements ICourseResourceService {

    @Override
    public Long addResource(CourseResource resource) {
        save(resource);
        return resource.getId();
    }

    @Override
    public Boolean updateResource(CourseResource resource) {
        return updateById(resource);
    }

    @Override
    public Boolean deleteResource(IdRequest idRequest) {
        return removeById(idRequest.getId());
    }

    @Override
    public CourseResource getResourceById(Long id) {
        return getById(id);
    }

    @Override
    public Page<CourseResource> listResources(int current, int size, Long courseId, Byte resourceType) {
        LambdaQueryWrapper<CourseResource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(courseId != null, CourseResource::getCourseId, courseId);
        wrapper.eq(resourceType != null, CourseResource::getResourceType, resourceType);
        wrapper.orderByDesc(CourseResource::getCreateTime);
        return page(new Page<>(current, size), wrapper);
    }

    @Override
    public Boolean incrementDownloadCount(Long id) {
        baseMapper.incrementDownloadCount(id);
        return true;
    }

    @Override
    public List<CourseResource> getResourcesByCourse(Long courseId) {
        return lambdaQuery()
                .eq(CourseResource::getCourseId, courseId)
                .orderByDesc(CourseResource::getCreateTime)
                .list();
    }

    @Override
    public LambdaQueryWrapper<CourseResource> getQueryWrapper(CourseResourceQueryRequest queryRequest) {
        LambdaQueryWrapper<CourseResource> wrapper = new LambdaQueryWrapper<>();
        if (queryRequest == null) {
            return wrapper;
        }
        Long id = queryRequest.getId();
        Long courseId = queryRequest.getCourseId();
        String resourceName = queryRequest.getResourceName();
        Integer resourceType = queryRequest.getResourceType();
        Long uploaderId = queryRequest.getUploaderId();

        wrapper.eq(ObjectUtils.isNotEmpty(id), CourseResource::getId, id);
        wrapper.eq(ObjectUtils.isNotEmpty(courseId), CourseResource::getCourseId, courseId);
        wrapper.like(StringUtils.isNotBlank(resourceName), CourseResource::getResourceName, resourceName);
        wrapper.eq(ObjectUtils.isNotEmpty(resourceType), CourseResource::getResourceType, resourceType);
        wrapper.eq(ObjectUtils.isNotEmpty(uploaderId), CourseResource::getUploaderId, uploaderId);

        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();
        if (StringUtils.isNotBlank(sortField)) {
            wrapper.orderBy(SqlUtils.validSortField(sortField),
                    sortOrder.equals("ascending"),
                    CourseResource::getCreateTime);
        }
        return wrapper;
    }
}
