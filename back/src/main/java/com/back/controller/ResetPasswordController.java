package com.back.controller;

import com.back.dto.ResetPasswordRequest;
import com.back.dto.Response;
import com.back.resetPassword.ResetMail;
import com.back.resetPassword.ResetPassword;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/reset")
public class ResetPasswordController {
    @Resource
    private ResetMail resetMail;
    @Resource
    private ResetPassword resetPassword;

    @Description("根据邮箱发送验证码")
    @PostMapping("/mailForReset")
    public Response mailMail(@RequestParam("email") String email) {
        return resetMail.mailMail(email);
    }

    @Description("收到验证码并且将用户名、密码、验证码输入完毕后注册")
    @PostMapping("/writeUser")
    public Response registerByEmail(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        return resetPassword.resetPassword(resetPasswordRequest);
    }
}
