package com.rosy.common.common;

import com.rosy.common.domain.entity.ApiResponse;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private T data;

    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(int code, String msg) {
        this(code, msg, null);
    }

    public BaseResponse(T data) {
        this(0, "success", data);
    }

    public BaseResponse() {
        this(0, "success", null);
    }
}
