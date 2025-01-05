package com.back.login;

import cn.hutool.crypto.SecureUtil;
import com.back.dto.LoginRequest;
import com.back.dto.Response;
import com.back.mail.encoding.Encode;
import com.back.redis.RedisCacheServiceImpl;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public abstract class Login {
    @Resource
    protected RedisCacheServiceImpl redisCacheService;
    @Resource
    protected Encode encode;
    @Resource
    protected UserRepository userRepository;

    public Response login(LoginRequest loginRequest) {
        if(!isUserExist(loginRequest.getUserUniqueIdentifier())){
            return Response.notFoundUser();
        } else if (!isPasswordCorrect(loginRequest.getPassword(),loginRequest.getUserUniqueIdentifier())) {
            return Response.errorPassword();
        }
        return Response.successLogin(generateToken(loginRequest));
    }

    private String generateToken(LoginRequest loginRequest) {
        String token = SecureUtil.md5(loginRequest.toString());
        redisCacheService.set(token,loginRequest.getUserUniqueIdentifier());
        return token;
    }
    protected abstract boolean isUserExist(String userUniqueIdentifier);
    protected abstract String getPassword(String userUniqueIdentifier);
    private boolean isPasswordCorrect(String password,String userUniqueIdentifier) {
        return encode.matches(password, getPassword(userUniqueIdentifier));
    }
}
