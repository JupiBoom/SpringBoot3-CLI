package com.rosy.main.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageRequest extends BaseVO {

    private static final long serialVersionUID = 1L;

    private Integer pageNum = 1;
    
    private Integer pageSize = 10;
}