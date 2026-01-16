package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.HomeworkSubmit;

public interface IHomeworkSubmitService extends IService<HomeworkSubmit> {

    boolean submitHomework(Long homeworkId, Long studentId, String content, String fileUrl);

    boolean gradeHomework(Long submitId, Double score, String feedback);
}
