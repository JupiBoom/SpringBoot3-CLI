package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;

/**
 * 用户Service接口
 */
public interface IUserService extends IService<User> {

    /**
     * 分页查询用户
     * @param page 分页参数
     * @param username 用户名（可选）
     * @param realName 真实姓名（可选）
     * @param department 部门（可选）
     * @param role 角色（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    Page<UserVO> getUserPage(Page<User> page, String username, String realName, String department, Integer role, Integer status);

    /**
     * 根据ID获取用户详情
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getUserById(Long id);

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 添加用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean addUser(User user);

    /**
     * 更新用户
     * @param user 用户信息
     * @return 是否成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(Long id);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 启用/禁用用户
     * @param userId 用户ID
     * @param status 状态：0-禁用，1-启用
     * @return 是否成功
     */
    boolean updateUserStatus(Long userId, Integer status);
}