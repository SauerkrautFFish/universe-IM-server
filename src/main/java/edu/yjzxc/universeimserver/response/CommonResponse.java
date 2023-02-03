package edu.yjzxc.universeimserver.response;

import edu.yjzxc.universeimserver.enums.ResponseEnum;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CommonResponse<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    private CommonResponse(Integer code) {
        this.code = code;
    }

    private CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    private CommonResponse(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public static <T> CommonResponse<T> successCode() {
        return new CommonResponse<>(ResponseEnum.SUCCESS.getCode());
    }

    public static <T> CommonResponse<T> successMsg(String msg) {
        return new CommonResponse<T>(ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static <T> CommonResponse<T> successData(T data) {
        return new CommonResponse<T>(ResponseEnum.SUCCESS.getCode(), data);
    }

    public static <T> CommonResponse<T> status(ResponseEnum responseEnum) {
        return new CommonResponse<T>(responseEnum);
    }

    public static <T> CommonResponse<T> errMsg(String errMsg) {
        return new CommonResponse<T>(ResponseEnum.SYSTEM_ERROR.getCode(), errMsg);
    }


}
