package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.product.ProductQueryRequest;
import com.rosy.main.domain.entity.Product;
import com.rosy.main.domain.vo.ProductVO;
import com.rosy.main.mapper.ProductMapper;
import com.rosy.main.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Override
    public ProductVO getProductVO(Product product) {
        return Optional.ofNullable(product)
                .map(p -> BeanUtil.copyProperties(p, ProductVO.class))
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<Product> getQueryWrapper(ProductQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), Product::getId);
        QueryWrapperUtil.addLikeCondition(queryWrapper, queryRequest.getName(), Product::getName);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), Product::getStatus);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                Product::getId);

        return queryWrapper;
    }
}
