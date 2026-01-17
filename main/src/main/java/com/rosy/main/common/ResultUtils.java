package com.rosy.main.common;

public class ResultUtils {

    private static final int SUCCESS_CODE = 200;
    private static final String SUCCESS_MESSAGE = "成功";
    private static final int ERROR_CODE = 500;

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(SUCCESS_CODE, message, data);
    }

    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(ERROR_CODE, message, null);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message, null);
    }
}