package com.rosy.volunteer.controller;

import com.rosy.common.domain.entity.PageRequest;
import com.rosy.volunteer.domain.vo.ActivityPhotoVO;
import com.rosy.volunteer.service.IActivityPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "活动照片管理")
@RestController
@RequestMapping("/api/volunteer/photo")
@RequiredArgsConstructor
public class ActivityPhotoController {

    private final IActivityPhotoService photoService;

    @Operation(summary = "上传活动照片")
    @PostMapping
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Long> uploadPhoto(@RequestBody Map<String, Object> request) {
        Long activityId = Long.valueOf(request.get("activityId").toString());
        String photoUrl = (String) request.get("photoUrl");
        String thumbnailUrl = (String) request.get("thumbnailUrl");
        String description = (String) request.get("description");
        Long id = photoService.uploadPhoto(activityId, photoUrl, thumbnailUrl, description);
        return ResponseEntity.ok(id);
    }

    @Operation(summary = "删除照片")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "更新照片排序")
    @PutMapping("/activity/{activityId}/sort")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePhotoSort(
            @PathVariable Long activityId,
            @RequestBody List<Map<String, Object>> sortList) {
        photoService.updatePhotoSort(activityId, sortList);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "点赞照片")
    @PostMapping("/{id}/like")
    @PreAuthorize("hasRole('VOLUNTEER') or hasRole('ADMIN')")
    public ResponseEntity<Void> likePhoto(@PathVariable Long id) {
        photoService.likePhoto(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取照片详情")
    @GetMapping("/{id}")
    public ResponseEntity<ActivityPhotoVO> getPhotoDetail(@PathVariable Long id) {
        ActivityPhotoVO vo = photoService.getPhotoDetail(id);
        return ResponseEntity.ok(vo);
    }

    @Operation(summary = "获取照片列表")
    @GetMapping("/list")
    public ResponseEntity<List<ActivityPhotoVO>> getPhotoList(
            @Parameter(description = "活动ID") @RequestParam(required = false) Long activityId,
            @Parameter(description = "上传者ID") @RequestParam(required = false) Long uploaderId) {
        List<ActivityPhotoVO> list = photoService.getPhotoList(activityId, uploaderId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "分页获取照片")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getPhotoPage(
            @Parameter(description = "活动ID") @RequestParam(required = false) Long activityId,
            @Parameter(description = "上传者ID") @RequestParam(required = false) Long uploaderId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pageNum);
        pageRequest.setPageSize(pageSize);
        Map<String, Object> result = (Map<String, Object>) photoService.getPhotoPage(activityId, uploaderId, pageRequest);
        return ResponseEntity.ok(result);
    }
}
