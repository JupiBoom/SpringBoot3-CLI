package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Page<UserVO> getUserPage(Page<User> page, String username, String realName, String department, Integer role, Integer status) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            queryWrapper.like("username", username);
        }
        
        if (StringUtils.hasText(realName)) {
            queryWrapper.like("real_name", realName);
        }
        
        if (StringUtils.hasText(department)) {
            queryWrapper.like("department", department);
        }
        
        if (role != null) {
            queryWrapper.eq("role", role);
        }
        
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        Page<User> userPage = this.page(page, queryWrapper);
        
        // 转换为VO
        Page<UserVO> voPage = new Page<>();
        BeanUtils.copyProperties(userPage, voPage);
        
        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        voPage.setRecords(voList);
        
        return voPage;
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = this.getById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    @Override
    public User getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("status", 1); // 只查询启用的用户
        
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean addUser(User user) {
        // 检查用户名是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        User existingUser = this.getOne(queryWrapper);
        
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 加密密码
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // 默认状态为启用
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        // 默认角色为普通用户
        if (user.getRole() == null) {
            user.setRole(0);
        }
        
        return this.save(user);
    }

    @Override
    public boolean updateUser(User user) {
        // 检查用户名是否重复（排除自己）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        queryWrapper.ne("id", user.getId());
        User existingUser = this.getOne(queryWrapper);
        
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 如果密码不为空，则加密密码
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 如果密码为空，则不更新密码
            user.setPassword(null);
        }
        
        return this.updateById(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        // 检查是否有关联的预约记录
        // 这里可以添加检查逻辑，如果有关联的预约记录，则不允许删除
        
        return this.removeById(id);
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码不正确");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        
        return this.updateById(user);
    }

    @Override
    public boolean resetPassword(Long userId, String newPassword) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        
        return this.updateById(user);
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        user.setStatus(status);
        
        return this.updateById(user);
    }

    /**
     * 实体转VO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        
        // 设置角色描述
        if (user.getRole() != null) {
            switch (user.getRole()) {
                case 0:
                    vo.setRoleDesc("普通用户");
                    break;
                case 1:
                    vo.setRoleDesc("管理员");
                    break;
            }
        }
        
        // 设置状态描述
        if (user.getStatus() != null) {
            switch (user.getStatus()) {
                case 0:
                    vo.setStatusDesc("禁用");
                    break;
                case 1:
                    vo.setStatusDesc("启用");
                    break;
            }
        }
        
        return vo;
    }
}