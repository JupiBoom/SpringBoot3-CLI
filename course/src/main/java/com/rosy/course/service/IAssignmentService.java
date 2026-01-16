package com.rosy.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.course.domain.entity.Assignment;

/**
 * <p>
 * 作业表 Service 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
public interface IAssignmentService extends IService<Assignment> {

    /**
     * 发布作业
     */
    boolean publishAssignment(Assignment assignment);

}