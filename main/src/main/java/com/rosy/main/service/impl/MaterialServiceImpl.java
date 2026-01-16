package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.Material;
import com.rosy.main.domain.vo.MaterialVO;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.MaterialMapper;
import com.rosy.main.service.IMaterialService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements IMaterialService {

    @Resource
    private CourseMapper courseMapper;

    @Override
    public MaterialVO getMaterialVO(Material material) {
        if (material == null) {
            return null;
        }
        MaterialVO materialVO = BeanUtil.copyProperties(material, MaterialVO.class);
        if (material.getCourseId() != null) {
            Course course = courseMapper.selectById(material.getCourseId());
            if (course != null) {
                materialVO.setCourseName(course.getName());
            }
        }
        return materialVO;
    }

    @Override
    public LambdaQueryWrapper<Material> getQueryWrapper(Long courseId, String title) {
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(courseId)) {
            queryWrapper.eq(Material::getCourseId, courseId);
        }
        if (ObjectUtil.isNotEmpty(title)) {
            queryWrapper.like(Material::getTitle, title);
        }
        queryWrapper.orderByDesc(Material::getCreateTime);
        return queryWrapper;
    }

    @Override
    public boolean uploadMaterial(Long courseId, String title, String description, String fileUrl, Long fileSize, String fileType) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        Material material = new Material();
        material.setCourseId(courseId);
        material.setTitle(title);
        material.setDescription(description);
        material.setFileUrl(fileUrl);
        material.setFileSize(fileSize);
        material.setFileType(fileType);
        return this.save(material);
    }

    @Override
    public boolean deleteMaterial(Long id) {
        Material material = this.getById(id);
        if (material == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "资料不存在");
        }
        return this.removeById(id);
    }

    @Override
    public List<MaterialVO> getMaterialsByCourse(Long courseId) {
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Material::getCourseId, courseId);
        wrapper.orderByDesc(Material::getCreateTime);
        return this.list(wrapper).stream()
                .map(this::getMaterialVO)
                .collect(Collectors.toList());
    }
}
