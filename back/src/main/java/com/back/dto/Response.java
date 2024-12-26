package com.back.dto;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String msg;
    public static Response success() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("success");
        return result;
    }

    public static  Response Forbidden(HttpStatus status) {
        Response result = new Response();
        result.setCode(status.getCode());
        result.setMsg(status.getMsg());
        return result;
    }
}
