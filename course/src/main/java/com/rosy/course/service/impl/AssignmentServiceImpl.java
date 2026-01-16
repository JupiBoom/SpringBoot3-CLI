package com.rosy.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.course.domain.entity.Assignment;
import com.rosy.course.mapper.AssignmentMapper;
import com.rosy.course.service.IAssignmentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业表 Service 实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Service
public class AssignmentServiceImpl extends ServiceImpl<AssignmentMapper, Assignment> implements IAssignmentService {

    @Override
    public boolean publishAssignment(Assignment assignment) {
        assignment.setStatus((byte) 1);
        return this.save(assignment);
    }

}