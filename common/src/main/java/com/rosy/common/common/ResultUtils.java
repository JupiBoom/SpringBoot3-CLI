package com.rosy.common.common;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;

public class ResultUtils {

    public static ApiResponse success(Object data) {
        ApiResponse response = new ApiResponse();
        response.put("code", ErrorCode.SUCCESS.getCode());
        response.put("msg", "success");
        response.put("data", data);
        return response;
    }

    public static ApiResponse error(ErrorCode errorCode) {
        ApiResponse response = new ApiResponse();
        response.put("code", errorCode.getCode());
        response.put("msg", errorCode.getMessage());
        return response;
    }

    public static ApiResponse error(int code, String msg) {
        ApiResponse response = new ApiResponse();
        response.put("code", code);
        response.put("msg", msg);
        return response;
    }
}
