package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.RepairPhoto;
import com.rosy.main.mapper.RepairPhotoMapper;
import com.rosy.main.service.IRepairPhotoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepairPhotoServiceImpl extends ServiceImpl<RepairPhotoMapper, RepairPhoto> implements IRepairPhotoService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePhotos(Long orderId, List<String> photoUrls, Byte photoType, Long userId) {
        if (photoUrls == null || photoUrls.isEmpty()) {
            return;
        }

        List<RepairPhoto> photos = new ArrayList<>();
        for (String photoUrl : photoUrls) {
            RepairPhoto photo = new RepairPhoto();
            photo.setOrderId(orderId);
            photo.setPhotoUrl(photoUrl);
            photo.setPhotoType(photoType);
            photo.setFileName(photoUrl.substring(photoUrl.lastIndexOf("/") + 1));
            photos.add(photo);
        }

        if (!saveBatch(photos)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存照片失败");
        }
    }

    @Override
    public List<String> getPhotoUrlsByOrderId(Long orderId) {
        List<RepairPhoto> photos = lambdaQuery()
                .eq(RepairPhoto::getOrderId, orderId)
                .list();
        return photos.stream()
                .map(RepairPhoto::getPhotoUrl)
                .toList();
    }

    @Override
    public List<String> getPhotoUrlsByOrderIdAndType(Long orderId, Byte photoType) {
        List<RepairPhoto> photos = lambdaQuery()
                .eq(RepairPhoto::getOrderId, orderId)
                .eq(RepairPhoto::getPhotoType, photoType)
                .list();
        return photos.stream()
                .map(RepairPhoto::getPhotoUrl)
                .toList();
    }
}
