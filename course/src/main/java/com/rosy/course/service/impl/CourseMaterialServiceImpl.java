package com.rosy.course.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.course.domain.entity.CourseMaterial;
import com.rosy.course.mapper.CourseMaterialMapper;
import com.rosy.course.service.ICourseMaterialService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程资料表 Service 实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Service
public class CourseMaterialServiceImpl extends ServiceImpl<CourseMaterialMapper, CourseMaterial> implements ICourseMaterialService {

    @Override
    public boolean downloadMaterial(Long id) {
        CourseMaterial material = this.getById(id);
        if (ObjectUtil.isNull(material)) {
            return false;
        }
        
        material.setDownloadCount(material.getDownloadCount() + 1);
        return this.updateById(material);
    }

}