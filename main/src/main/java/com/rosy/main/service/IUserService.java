package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;

public interface IUserService extends IService<User> {

    UserVO getUserVO(User user);

    User getByUsername(String username);

    boolean updateTotalHours(Long userId);
}
