package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveRoomExplanation;
import com.rosy.main.mapper.LiveRoomExplanationMapper;
import com.rosy.main.service.ILiveRoomExplanationService;
import org.springframework.stereotype.Service;

@Service
public class LiveRoomExplanationServiceImpl extends ServiceImpl<LiveRoomExplanationMapper, LiveRoomExplanation> implements ILiveRoomExplanationService {
}