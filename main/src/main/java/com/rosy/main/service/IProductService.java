package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.product.ProductQueryRequest;
import com.rosy.main.domain.entity.Product;
import com.rosy.main.domain.vo.ProductVO;

public interface IProductService extends IService<Product> {

    ProductVO getProductVO(Product product);

    LambdaQueryWrapper<Product> getQueryWrapper(ProductQueryRequest queryRequest);
}
