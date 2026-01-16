package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.CourseMaterial;
import com.rosy.main.mapper.CourseMaterialMapper;
import com.rosy.main.service.ICourseMaterialService;
import org.springframework.stereotype.Service;

@Service
public class CourseMaterialServiceImpl extends ServiceImpl<CourseMaterialMapper, CourseMaterial> implements ICourseMaterialService {
}
