package com.rosy.common.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
}
