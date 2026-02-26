package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.AudienceRecord;

import java.util.List;

public interface IAudienceRecordService extends IService<AudienceRecord> {

    boolean recordAudience(Long liveRoomId, Integer viewerCount, Integer newViewerCount, Integer leaveViewerCount);

    List<AudienceRecord> getAudienceRecords(Long liveRoomId);

    Integer getPeakViewerCount(Long liveRoomId);
}
