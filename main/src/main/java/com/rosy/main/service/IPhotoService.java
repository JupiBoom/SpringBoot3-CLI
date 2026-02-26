package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.photo.PhotoQueryRequest;
import com.rosy.main.domain.entity.Photo;
import com.rosy.main.domain.vo.PhotoVO;

public interface IPhotoService extends IService<Photo> {

    PhotoVO getPhotoVO(Photo photo);

    LambdaQueryWrapper<Photo> getQueryWrapper(PhotoQueryRequest queryRequest);

    boolean incrementLikeCount(Long photoId);
}
