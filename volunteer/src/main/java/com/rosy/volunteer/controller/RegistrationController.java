package com.rosy.volunteer.controller;

import com.rosy.volunteer.domain.dto.RegistrationCreateDTO;
import com.rosy.volunteer.domain.dto.RegistrationReviewDTO;
import com.rosy.volunteer.domain.vo.RegistrationVO;
import com.rosy.volunteer.service.IRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "活动报名管理")
@RestController
@RequestMapping("/api/volunteer/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final IRegistrationService registrationService;

    @Operation(summary = "创建活动报名")
    @PostMapping
    public ResponseEntity<Long> createRegistration(@RequestBody RegistrationCreateDTO dto) {
        Long id = registrationService.createRegistration(dto);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "取消活动报名")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long id) {
        registrationService.cancelRegistration(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "审核活动报名")
    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> reviewRegistration(
            @PathVariable Long id,
            @RequestBody RegistrationReviewDTO dto) {
        registrationService.reviewRegistration(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "自动审核活动报名")
    @PutMapping("/{id}/auto-review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> autoReviewRegistration(@PathVariable Long id) {
        registrationService.autoReviewRegistration(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "签到")
    @PutMapping("/{id}/checkin")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> checkIn(@PathVariable Long id) {
        registrationService.checkIn(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "签出")
    @PutMapping("/{id}/checkout")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> checkOut(@PathVariable Long id) {
        registrationService.checkOut(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取报名记录详情")
    @GetMapping("/{id}")
    public ResponseEntity<RegistrationVO> getRegistrationDetail(@PathVariable Long id) {
        RegistrationVO vo = registrationService.getRegistrationDetail(id);
        return ResponseEntity.ok(vo);
    }

    @Operation(summary = "获取活动报名列表")
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Page<RegistrationVO>> getRegistrationListByActivity(
            @PathVariable Long activityId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RegistrationVO> result = registrationService.getRegistrationListByActivity(activityId, status, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取我的报名记录")
    @GetMapping("/my")
    @PreAuthorize("hasRole('VOLUNTEER')")
    public ResponseEntity<Page<RegistrationVO>> getMyRegistrationList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RegistrationVO> result = registrationService.getMyRegistrationList(status, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "发送活动提醒")
    @PostMapping("/activity/{activityId}/reminder")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<Void> sendActivityReminder(@PathVariable Long activityId) {
        registrationService.sendActivityReminder(activityId);
        return ResponseEntity.ok().build();
    }
}
