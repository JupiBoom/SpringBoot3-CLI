package com.rosy.volunteer.controller;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.dto.ServiceRatingDTO;
import com.rosy.volunteer.domain.vo.ServiceRecordDetailVO;
import com.rosy.volunteer.domain.vo.ServiceRecordListVO;
import com.rosy.volunteer.service.IServiceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "服务记录管理")
@RestController
@RequestMapping("/api/volunteer/service-record")
@RequiredArgsConstructor
public class ServiceRecordController {

    private final IServiceRecordService serviceRecordService;

    @Operation(summary = "创建服务记录")
    @PostMapping("/registration/{registrationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> createServiceRecord(@PathVariable Long registrationId) {
        serviceRecordService.createServiceRecord(registrationId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "更新服务时长")
    @PutMapping("/{id}/duration")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> updateDuration(@PathVariable Long id) {
        serviceRecordService.updateDuration(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "服务评价")
    @PostMapping("/{id}/rating")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> rateService(@PathVariable Long id, @RequestBody ServiceRatingDTO dto) {
        serviceRecordService.rateService(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "生成服务证明")
    @PostMapping("/{id}/certificate")
    public ResponseEntity<String> generateCertificate(@PathVariable Long id) {
        String url = serviceRecordService.generateCertificate(id);
        return ResponseEntity.ok(url);
    }

    @Operation(summary = "获取服务记录详情")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRecordDetailVO> getServiceRecordDetail(@PathVariable Long id) {
        ServiceRecordDetailVO vo = serviceRecordService.getServiceRecordDetail(id);
        return ResponseEntity.ok(vo);
    }

    @Operation(summary = "获取服务记录列表")
    @GetMapping("/list")
    public ResponseEntity<List<ServiceRecordListVO>> getServiceRecordList(
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long volunteerId) {
        List<ServiceRecordListVO> list = serviceRecordService.getServiceRecordList(activityId, volunteerId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "分页获取服务记录")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getServiceRecordPage(
            @RequestParam(required = false) Long activityId,
            @RequestParam(required = false) Long volunteerId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pageNum);
        pageRequest.setPageSize(pageSize);
        Map<String, Object> result = (Map<String, Object>) serviceRecordService.getServiceRecordPage(activityId, volunteerId, pageRequest);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取志愿者统计信息")
    @GetMapping("/statistics/{volunteerId}")
    public ResponseEntity<Map<String, Object>> getVolunteerStatistics(@PathVariable Long volunteerId) {
        Map<String, Object> statistics = (Map<String, Object>) serviceRecordService.getVolunteerStatistics(volunteerId);
        return ResponseEntity.ok(statistics);
    }

    @Operation(summary = "获取我的服务记录统计")
    @GetMapping("/statistics/my")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<Map<String, Object>> getMyStatistics() {
        Long volunteerId = 1L;
        Map<String, Object> statistics = (Map<String, Object>) serviceRecordService.getVolunteerStatistics(volunteerId);
        return ResponseEntity.ok(statistics);
    }
}
