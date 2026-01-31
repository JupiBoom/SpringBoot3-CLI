package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.resource.CourseResourceQueryRequest;
import com.rosy.main.domain.entity.CourseResource;

import java.util.List;

public interface ICourseResourceService extends IService<CourseResource> {

    Long addResource(CourseResource resource);

    Boolean updateResource(CourseResource resource);

    Boolean deleteResource(IdRequest idRequest);

    CourseResource getResourceById(Long id);

    Page<CourseResource> listResources(int current, int size, Long courseId, Byte resourceType);

    Boolean incrementDownloadCount(Long id);

    LambdaQueryWrapper<CourseResource> getQueryWrapper(CourseResourceQueryRequest queryRequest);

    List<CourseResource> getResourcesByCourse(Long courseId);
}