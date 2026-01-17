package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;

public interface IUserService extends IService<User> {

    UserVO getUserVO(User user);

    LambdaQueryWrapper<User> getQueryWrapper(String username, String realName, Byte role, Byte status);

    User getUserByUsername(String username);
}
