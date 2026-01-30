package com.rosy.web.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;
import com.rosy.main.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final IUserService userService;

    @GetMapping("/page")
    @Operation(summary = "分页查询用户", description = "根据条件分页查询用户列表")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<UserVO>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "真实姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "部门") @RequestParam(required = false) String department,
            @Parameter(description = "角色") @RequestParam(required = false) Integer role,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        Page<User> page = new Page<>(current, size);
        Page<UserVO> result = userService.getUserPage(page, username, realName, department, role, status);
        
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ApiResponse<UserVO> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }
        return ApiResponse.success(user);
    }

    @PostMapping
    @Operation(summary = "添加用户", description = "添加新用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Boolean> addUser(@RequestBody User user) {
        try {
            boolean result = userService.addUser(user);
            return result ? Result.success(true) : Result.error("添加用户失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "更新用户", description = "更新用户信息")
    @PreAuthorize("hasRole('ADMIN') or #user.id == authentication.principal.id")
    public Result<Boolean> updateUser(@RequestBody User user) {
        try {
            boolean result = userService.updateUser(user);
            return result ? Result.success(true) : Result.error("更新用户失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据ID删除用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        boolean result = userService.deleteUser(id);
        return result ? Result.success(true) : Result.error("删除用户失败");
    }

    @PutMapping("/change-password")
    @Operation(summary = "修改密码", description = "用户自己修改密码")
    public Result<Boolean> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            boolean result = userService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
            return result ? Result.success(true) : Result.error("修改密码失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/reset-password")
    @Operation(summary = "重置密码", description = "管理员重置用户密码")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            boolean result = userService.resetPassword(request.getUserId(), request.getNewPassword());
            return result ? Result.success(true) : Result.error("重置密码失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/status")
    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Boolean> updateUserStatus(@RequestBody UpdateStatusRequest request) {
        try {
            boolean result = userService.updateUserStatus(request.getUserId(), request.getStatus());
            return result ? Result.success(true) : Result.error("更新用户状态失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码请求参数
     */
    public static class ChangePasswordRequest {
        private Long userId;
        private String oldPassword;
        private String newPassword;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    /**
     * 重置密码请求参数
     */
    public static class ResetPasswordRequest {
        private Long userId;
        private String newPassword;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    /**
     * 更新状态请求参数
     */
    public static class UpdateStatusRequest {
        private Long userId;
        private Integer status;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}