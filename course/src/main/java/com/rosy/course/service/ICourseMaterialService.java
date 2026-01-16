package com.rosy.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.course.domain.entity.CourseMaterial;

/**
 * <p>
 * 课程资料表 Service 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
public interface ICourseMaterialService extends IService<CourseMaterial> {

    /**
     * 下载资料（增加下载次数）
     */
    boolean downloadMaterial(Long id);

}