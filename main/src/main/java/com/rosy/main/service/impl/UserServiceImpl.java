package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.GenderEnum;
import com.rosy.common.enums.UserRoleEnum;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.UserVO;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = BeanUtil.copyProperties(user, UserVO.class);

        GenderEnum genderEnum = GenderEnum.getByCode(user.getGender());
        if (genderEnum != null) {
            vo.setGenderDesc(genderEnum.getDesc());
        }

        UserRoleEnum roleEnum = UserRoleEnum.getByCode(user.getRole());
        if (roleEnum != null) {
            vo.setRoleDesc(roleEnum.getText());
        }

        return vo;
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean updateTotalHours(Long userId) {
        return false;
    }
}
