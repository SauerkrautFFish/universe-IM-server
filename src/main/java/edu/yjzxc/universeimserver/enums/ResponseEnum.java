package edu.yjzxc.universeimserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(0, "成功"),

    //-1xx 服务器错误
    SYSTEM_ERROR(-1, "系统异常!"),
    EMAIL_SERVICE_ERROR(-101, "邮件服务异常!"),
    //-2xx 参数校验
    EMAIL_NULL_INCORRECT(-201, "邮箱地址不能为空!"),
    EMAIL_FORMAT_INCORRECT(-202, "邮箱地址格式不正确!"),

    //-3
    ACCOUNT_EXISTS(-301, "该邮箱账号已存在!"),
    ;

    private Integer code;

    private String message;

    public static ResponseEnum getResEnum(Integer code) {
        ResponseEnum[] values = values();
        for(int i = 0; i < values.length; i++) {
            if(values[i].getCode() == code) {
                return values[i];
            }
        }

        return null;
    }

}
