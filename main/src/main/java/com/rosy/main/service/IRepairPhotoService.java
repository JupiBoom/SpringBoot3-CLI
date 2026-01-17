package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairPhoto;

import java.util.List;

public interface IRepairPhotoService extends IService<RepairPhoto> {

    void savePhotos(Long orderId, List<String> photoUrls, Byte photoType, Long userId);

    List<String> getPhotoUrlsByOrderId(Long orderId);

    List<String> getPhotoUrlsByOrderIdAndType(Long orderId, Byte photoType);
}
