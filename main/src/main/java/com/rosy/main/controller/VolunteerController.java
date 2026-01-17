package com.rosy.main.controller;

import com.rosy.main.common.BaseResponse;
import com.rosy.main.common.ResultUtils;
import com.rosy.main.domain.vo.VolunteerVO;
import com.rosy.main.dto.req.VolunteerAddRequest;
import com.rosy.main.dto.req.VolunteerUpdateRequest;
import com.rosy.main.dto.req.VolunteerQueryRequest;
import com.rosy.main.service.IVolunteerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volunteer")
@RequiredArgsConstructor
@Validated
public class VolunteerController {

    private final IVolunteerService volunteerService;

    @PostMapping
    public BaseResponse<Long> addVolunteer(@Valid @RequestBody VolunteerAddRequest request) {
        Long id = volunteerService.addVolunteer(request);
        return ResultUtils.success(id);
    }

    @PutMapping
    public BaseResponse<Void> updateVolunteer(@Valid @RequestBody VolunteerUpdateRequest request) {
        volunteerService.updateVolunteer(request);
        return ResultUtils.success();
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteVolunteer(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        volunteerService.deleteVolunteer(id);
        return ResultUtils.success();
    }

    @GetMapping("/{id}")
    public BaseResponse<VolunteerVO> getVolunteerVO(@PathVariable @NotNull(message = "ID不能为空") Long id) {
        VolunteerVO volunteerVO = volunteerService.getVolunteerVO(id);
        return ResultUtils.success(volunteerVO);
    }

    @GetMapping("/list")
    public BaseResponse<List<VolunteerVO>> listVolunteerVO(@Valid VolunteerQueryRequest request) {
        List<VolunteerVO> volunteerVOList = volunteerService.listVolunteerVO(request);
        return ResultUtils.success(volunteerVOList);
    }

    @GetMapping("/phone/{phone}")
    public BaseResponse<VolunteerVO> getVolunteerByPhone(
            @PathVariable @NotBlank(message = "手机号不能为空") String phone) {
        VolunteerVO volunteerVO = volunteerService.getVolunteerByPhone(phone);
        return ResultUtils.success(volunteerVO);
    }

    @PostMapping("/{volunteerId}/duration")
    public BaseResponse<Void> updateVolunteerServiceDuration(
            @PathVariable @NotNull(message = "志愿者ID不能为空") Long volunteerId,
            @RequestParam @NotBlank(message = "时长不能为空") String serviceDuration) {
        volunteerService.updateVolunteerServiceDuration(volunteerId, serviceDuration);
        return ResultUtils.success();
    }

    @GetMapping("/ranking")
    public BaseResponse<List<VolunteerVO>> getVolunteerRanking(
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit) {
        List<VolunteerVO> ranking = volunteerService.getVolunteerRanking(limit);
        return ResultUtils.success(ranking);
    }
}