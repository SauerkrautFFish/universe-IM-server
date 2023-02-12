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
    MISSING_PARAMS(-203, "参数缺失, 请输入完整!"),
    //-3
    ACCOUNT_EXISTS(-301, "该邮箱账号已存在!"),
    RESEND_CODE(302, "请重新发送验证码!"),
    VERIFY_CODE_ERROR(303, "验证码错误"),
    ACCOUNT_NOT_EXISTS(304, "该邮箱账号不存在!"),
    PASSWORD_INCORRECT(305, "密码错误!"),
    TOKEN_ISSUE(306, "状态已失效, 请重新登录!"),
    ADD_FRIEND_YOURSELF(307, "不能添加自己为好友!"),
    ALREADY_FRIEND(308, "请勿重复添加!"),

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

    @Override
    public String toString() {
        return "ResponseEnum{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
