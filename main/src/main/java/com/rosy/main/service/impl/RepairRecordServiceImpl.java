package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.repair.RepairRecordAddRequest;
import com.rosy.main.domain.entity.RepairPhoto;
import com.rosy.main.domain.entity.RepairRecord;
import com.rosy.main.domain.vo.repair.RepairRecordVO;
import com.rosy.main.enums.PhotoTypeEnum;
import com.rosy.main.enums.RepairRecordTypeEnum;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairPhotoService;
import com.rosy.main.service.IRepairRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {

    @Resource
    private IRepairPhotoService repairPhotoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairRecordVO createRecord(RepairRecordAddRequest request, Long userId) {
        RepairRecord record = BeanUtil.copyProperties(request, RepairRecord.class);
        record.setOperatorId(userId);
        boolean saved = save(record);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建维修记录失败");
        }

        if (request.getPhotoUrls() != null && !request.getPhotoUrls().isEmpty()) {
            repairPhotoService.savePhotos(request.getOrderId(), request.getPhotoUrls(), PhotoTypeEnum.REPAIR_PROCESS.getCode().byteValue(), userId);
            String photos = String.join(",", request.getPhotoUrls());
            record.setPhotos(photos);
            updateById(record);
        }

        return getRepairRecordVO(record);
    }

    @Override
    public List<RepairRecordVO> getRecordsByOrderId(Long orderId) {
        List<RepairRecord> records = lambdaQuery()
                .eq(RepairRecord::getOrderId, orderId)
                .orderByAsc(RepairRecord::getCreateTime)
                .list();
        return records.stream()
                .map(this::getRepairRecordVO)
                .toList();
    }

    @Override
    public RepairRecordVO getRepairRecordVO(RepairRecord record) {
        if (record == null) {
            return null;
        }
        RepairRecordVO vo = BeanUtil.copyProperties(record, RepairRecordVO.class);
        if (record.getRecordType() != null) {
            RepairRecordTypeEnum recordTypeEnum = RepairRecordTypeEnum.getByCode(record.getRecordType().intValue());
            vo.setRecordTypeDesc(recordTypeEnum != null ? recordTypeEnum.getDesc() : "");
        }
        if (StrUtil.isNotBlank(record.getPhotos())) {
            vo.setPhotos(Arrays.asList(record.getPhotos().split(",")));
        }
        return vo;
    }
}
