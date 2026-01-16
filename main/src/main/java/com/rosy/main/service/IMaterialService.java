package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Material;
import com.rosy.main.domain.vo.MaterialVO;

import java.util.List;

public interface IMaterialService extends IService<Material> {

    MaterialVO getMaterialVO(Material material);

    LambdaQueryWrapper<Material> getQueryWrapper(Long courseId, String title);

    boolean uploadMaterial(Long courseId, String title, String description, String fileUrl, Long fileSize, String fileType);

    boolean deleteMaterial(Long id);

    List<MaterialVO> getMaterialsByCourse(Long courseId);
}
