package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.product.ProductAddRequest;
import com.rosy.main.domain.dto.product.ProductQueryRequest;
import com.rosy.main.domain.dto.product.ProductUpdateRequest;
import com.rosy.main.domain.entity.Product;
import com.rosy.main.domain.vo.ProductVO;
import com.rosy.main.service.IProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private IProductService productService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addProduct(@RequestBody ProductAddRequest addRequest) {
        Product product = BeanUtil.copyProperties(addRequest, Product.class);
        product.setSoldCount(0);
        boolean result = productService.save(product);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(product.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteProduct(@RequestBody IdRequest idRequest) {
        boolean result = productService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateProduct(@RequestBody ProductUpdateRequest updateRequest) {
        if (updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Product product = BeanUtil.copyProperties(updateRequest, Product.class);
        boolean result = productService.updateById(product);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Product product = productService.getById(id);
        ThrowUtils.throwIf(product == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(product);
    }

    @GetMapping("/get/vo")
    public ApiResponse getProductVOById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Product product = productService.getById(id);
        ThrowUtils.throwIf(product == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(productService.getProductVO(product));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listProductByPage(@RequestBody ProductQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<Product> page = productService.page(
                new Page<>(current, size),
                productService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listProductVOByPage(@RequestBody ProductQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Product> page = productService.page(
                new Page<>(current, size),
                productService.getQueryWrapper(queryRequest)
        );
        Page<ProductVO> voPage = PageUtils.convert(page, productService::getProductVO);
        return ApiResponse.success(voPage);
    }
}
