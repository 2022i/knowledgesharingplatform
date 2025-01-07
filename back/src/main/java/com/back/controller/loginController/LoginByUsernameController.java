package com.back.controller.loginController;

import com.back.dto.request.LoginRequest;
import com.back.dto.Response;
import com.back.login.LoginByUsername;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/login")
public class LoginByUsernameController {
    @Resource
    private LoginByUsername loginByUsername;

    @Description("根据用户名和密码登录")
    @PostMapping("/loginByUsername")
    public Response loginByUsername(@RequestBody LoginRequest loginRequest) {
        return loginByUsername.login(loginRequest);
    }
}
