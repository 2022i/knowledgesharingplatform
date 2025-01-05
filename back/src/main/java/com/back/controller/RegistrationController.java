package com.back.controller;

import com.back.dto.RegisterMailRequest;
import com.back.dto.RegistrationRequest;
import com.back.dto.Response;
import com.back.registration.RegisterMail;
import com.back.registration.WriteUser;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/register")
public class RegistrationController {
    @Resource
    private RegisterMail registerMail;
    @Resource
    private WriteUser writeUser;

    @Description("根据邮箱发送验证码")
    @PostMapping("/mailForRegister")
    public Response mailMail(@RequestBody RegisterMailRequest registerMailRequest) {
        return registerMail.mailMail(registerMailRequest);
    }

    @Description("收到验证码并且将用户名、密码、验证码输入完毕后注册")
    @PostMapping("/writeUser")
    public Response registerByEmail(@RequestBody RegistrationRequest registrationRequest) {
        return writeUser.registerByEmail(registrationRequest);
    }
}
