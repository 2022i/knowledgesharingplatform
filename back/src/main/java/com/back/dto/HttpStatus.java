package com.back.dto;

import lombok.Getter;

/**
 * HTTP 响应状态
 *
 * @author lzz
 */
@Getter
public enum HttpStatus {
    ERROR(500, "未知异常，请联系管理员"),
    BAD_REQUEST(400, "账号或密码错误"),
    FORBIDDEN(403, "禁止访问，请联系管理员"),
    ACCOUNT_EXPIRED(403, "账号或密码已过期"),
    ACCOUNT_LOCKED(403, "账号已锁定"),
    ACCOUNT_DISABLED(403, "账号已停用");

    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 状态码对应的含义
     */
    private final String msg;

    HttpStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
