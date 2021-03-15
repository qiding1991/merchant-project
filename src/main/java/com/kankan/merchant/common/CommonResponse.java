package com.kankan.merchant.common;

import lombok.Data;

@Data
public class CommonResponse<T> {
    private int code;
    private String message;
    private T data;

    public CommonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public CommonResponse() {

    }

    public static CommonResponse error(ErrorCode errorCode) {
        CommonResponse response = new CommonResponse();
        response.code = errorCode.getCode();
        response.message = errorCode.getMessage();
        return response;
    }

    public static CommonResponse error(ErrorCode errorCode,String message) {
        CommonResponse response = new CommonResponse();
        response.code = errorCode.getCode();
        response.message = message;
        return response;
    }


    public static CommonResponse success() {
        CommonResponse response = new CommonResponse();
        response.code = ErrorCode.SUCCESS.getCode();
        response.message = ErrorCode.SUCCESS.getMessage();
        return response;
    }

    public static <T> CommonResponse success(T data) {
        CommonResponse response = new CommonResponse();
        response.code = ErrorCode.SUCCESS.getCode();
        response.message = ErrorCode.SUCCESS.getMessage();
        response.data = data;
        return response;
    }
}
