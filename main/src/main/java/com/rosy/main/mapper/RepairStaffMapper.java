package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RepairStaff;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RepairStaffMapper extends BaseMapper<RepairStaff> {

    @Select("SELECT * FROM repair_staff WHERE status = 1 AND is_deleted = 0 ORDER BY order_count ASC LIMIT 1")
    RepairStaff selectAvailableStaffWithLeastOrders();

    @Select("SELECT * FROM repair_staff WHERE status = 1 AND specialty LIKE CONCAT('%', #{specialty}, '%') AND is_deleted = 0 ORDER BY order_count ASC LIMIT 1")
    RepairStaff selectAvailableStaffBySpecialty(String specialty);

    @Select("SELECT rs.*, AVG(re.rating) as avg_rating FROM repair_staff rs LEFT JOIN repair_evaluation re ON rs.id = re.staff_id WHERE rs.is_deleted = 0 GROUP BY rs.id ORDER BY avg_rating DESC")
    List<RepairStaff> selectStaffRanking();
}
