package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setRoleName(getRoleName(user.getRole()));
        userVO.setStatusName(getStatusName(user.getStatus()));
        return userVO;
    }

    @Override
    public LambdaQueryWrapper<User> getQueryWrapper(String username, String realName, Byte role, Byte status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            queryWrapper.like(User::getUsername, username);
        }
        if (realName != null && !realName.isEmpty()) {
            queryWrapper.like(User::getRealName, realName);
        }
        if (role != null) {
            queryWrapper.eq(User::getRole, role);
        }
        if (status != null) {
            queryWrapper.eq(User::getStatus, status);
        }
        queryWrapper.orderByDesc(User::getCreateTime);
        return queryWrapper;
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    private String getRoleName(Byte role) {
        if (role == null) {
            return "";
        }
        return switch (role) {
            case 1 -> "管理员";
            case 2 -> "志愿者";
            default -> "";
        };
    }

    private String getStatusName(Byte status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case 0 -> "禁用";
            case 1 -> "启用";
            default -> "";
        };
    }
}
