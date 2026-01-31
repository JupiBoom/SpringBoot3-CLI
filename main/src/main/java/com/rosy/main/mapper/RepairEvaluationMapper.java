package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairEvaluation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RepairEvaluationMapper extends BaseMapper<RepairEvaluation> {

    @Select("SELECT AVG(rating) FROM repair_evaluation WHERE staff_id = #{staffId} AND is_deleted = 0")
    Double getAvgRatingByStaffId(@Param("staffId") Long staffId);

    @Select("SELECT AVG(rating) FROM repair_evaluation WHERE is_deleted = 0")
    Double getOverallAvgRating();
}
