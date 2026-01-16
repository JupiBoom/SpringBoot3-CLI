package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.HomeworkSubmit;
import com.rosy.main.mapper.HomeworkSubmitMapper;
import com.rosy.main.service.IHomeworkSubmitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class HomeworkSubmitServiceImpl extends ServiceImpl<HomeworkSubmitMapper, HomeworkSubmit> implements IHomeworkSubmitService {

 @Override
 @Transactional(rollbackFor = Exception.class)
 public boolean submitHomework(Long homeworkId, Long studentId, String content, String fileUrl) {
 LambdaQueryWrapper<HomeworkSubmit> wrapper = new LambdaQueryWrapper<>();
 wrapper.eq(HomeworkSubmit::getHomeworkId, homeworkId)
 .eq(HomeworkSubmit::getStudentId, studentId);
 HomeworkSubmit exist = this.getOne(wrapper);
 if (exist != null) {
 exist.setContent(content);
 exist.setFileUrl(fileUrl);
 exist.setSubmitTime(LocalDateTime.now());
 exist.setStatus((byte) 1);
 return this.updateById(exist);
 }
 HomeworkSubmit submit = new HomeworkSubmit();
 submit.setHomeworkId(homeworkId);
 submit.setStudentId(studentId);
 submit.setContent(content);
 submit.setFileUrl(fileUrl);
 submit.setSubmitTime(LocalDateTime.now());
 submit.setStatus((byte) 1);
 return this.save(submit);
 }

 @Override
 @Transactional(rollbackFor = Exception.class)
 public boolean gradeHomework(Long submitId, Double score, String feedback) {
 HomeworkSubmit submit = this.getById(submitId);
 if (submit == null) {
 return false;
 }
 submit.setScore(BigDecimal.valueOf(score));
 submit.setFeedback(feedback);
 submit.setStatus((byte) 2);
 return this.updateById(submit);
 }
}
