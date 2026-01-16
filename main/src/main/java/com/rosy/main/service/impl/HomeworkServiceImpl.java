package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.mapper.HomeworkMapper;
import com.rosy.main.service.IHomeworkService;
import org.springframework.stereotype.Service;

@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework> implements IHomeworkService {
}
