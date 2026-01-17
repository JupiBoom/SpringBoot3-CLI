package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;
import com.rosy.main.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("/get")
    public ApiResponse getUserById(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(user);
    }

    @GetMapping("/get/vo")
    public ApiResponse getUserVOById(@RequestParam Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(userService.getUserVO(user));
    }

    @GetMapping("/get-by-username")
    public ApiResponse getUserByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return ApiResponse.success(user);
    }

    @GetMapping("/list/page/vo")
    public ApiResponse listUserVOByPage(@RequestParam(required = false) String username, @RequestParam(required = false) String realName, @RequestParam(required = false) Byte role, @RequestParam(required = false) Byte status, @RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size) {
        var queryWrapper = userService.getQueryWrapper(username, realName, role, status);
        Page<User> page = userService.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> voPage = PageUtils.convert(page, userService::getUserVO);
        return ApiResponse.success(voPage);
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteUser(@RequestBody IdRequest idRequest) {
        boolean result = userService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }
}
