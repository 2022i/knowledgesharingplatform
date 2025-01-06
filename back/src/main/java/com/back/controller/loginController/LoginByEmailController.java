package com.back.controller.loginController;

import com.back.dto.LoginRequest;
import com.back.dto.Response;
import com.back.login.LoginByEmail;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/login")
public class LoginByEmailController {
    @Resource
    private LoginByEmail loginByEmail;

    @Description("根据邮箱和密码登录")
    @PostMapping("/loginByEmail")
    public Response loginByEmail(@RequestBody LoginRequest loginRequest) {
        return loginByEmail.login(loginRequest);
    }
}
