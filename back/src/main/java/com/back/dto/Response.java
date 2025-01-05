package com.back.dto;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String msg;
    public static Response successAddDataAndSentMessage() {
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
    public static  Response successRegister() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("成功注册");
        return result;
    }

    public static  Response successMail() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("成功发送验证码");
        return result;
    }


    public static  Response alreadyExistsEmail() {
        Response result = new Response();
        result.setCode(409);
        result.setMsg("该邮箱已被使用");
        return result;
    }

    public static  Response alreadyExistsUsername() {
        Response result = new Response();
        result.setCode(409);
        result.setMsg("该用户名已被使用");
        return result;
    }

    public static  Response codeExpired() {
        Response result = new Response();
        result.setCode(401);
        result.setMsg("验证码已过期");
        return result;
    }

    public static  Response errorCode() {
        Response result = new Response();
        result.setCode(401);
        result.setMsg("验证码错误");
        return result;
    }

    public static Response successLogin(String token) {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("登录成功 "+" token:"+token);
        return result;
    }

    public static Response notFoundUser() {
        Response result = new Response();
        result.setCode(404);
        result.setMsg("用户不存在 ");
        return result;
    }

    public static Response errorPassword() {
        Response result = new Response();
        result.setCode(401);
        result.setMsg("密码错误");
        return result;
    }

    public static Response successResetPassword() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("密码重置成功");
        return result;
    }


}
