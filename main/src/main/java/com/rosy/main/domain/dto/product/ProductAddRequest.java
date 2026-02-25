package com.rosy.main.domain.dto.product;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;

    @Size(max = 500, message = "封面URL长度不能超过500个字符")
    private String coverUrl;

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "原价不能为负数")
    private BigDecimal originalPrice;

    @NotNull(message = "库存不能为空")
    @PositiveOrZero(message = "库存必须为非负整数")
    private Integer stock;

    @Size(max = 1000, message = "卖点长度不能超过1000个字符")
    private String sellingPoints;

    private String description;

    @Min(value = 0, message = "状态值只能为0或1")
    @Max(value = 1, message = "状态值只能为0或1")
    private Byte status = 1;
}
