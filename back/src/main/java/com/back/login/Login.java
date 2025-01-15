package com.back.login;

import cn.hutool.crypto.SecureUtil;
import com.back.dto.request.LoginRequest;
import com.back.dto.Response;
import com.back.mail.encoding.Encode;
import com.back.redis.RedisCacheServiceImpl;
import com.back.repository.AdministratorRepository;
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
    @Resource
    protected AdministratorRepository administratorRepository;

    public Response login(LoginRequest loginRequest) {
        if(!isAccountExit(loginRequest.getUserUniqueIdentifier())){
            return Response.notFoundUser();
        } else if(isUserExist(loginRequest.getUserUniqueIdentifier()) && isUserPasswordCorrect(loginRequest.getPassword(),loginRequest.getUserUniqueIdentifier())) {
            return Response.successLoginUser(generateToken(loginRequest),getUserId(loginRequest.getUserUniqueIdentifier()));
        }
        else if (isAdministratorExist(loginRequest.getUserUniqueIdentifier()) && isAdministratorPasswordCorrect(loginRequest.getPassword(),loginRequest.getUserUniqueIdentifier())) {
            return Response.successLoginAdministrator(generateToken(loginRequest));
        }
        return Response.errorPassword();
    }
    private boolean isAccountExit(String userUniqueIdentifier) {
        return isUserExist(userUniqueIdentifier) || isAdministratorExist(userUniqueIdentifier);
    }
    private String generateToken(LoginRequest loginRequest) {
        String token = SecureUtil.md5(loginRequest.toString());
        redisCacheService.set(token,loginRequest.getUserUniqueIdentifier());
        return token;
    }
    private boolean isAdministratorPasswordCorrect(String password, String userUniqueIdentifier) {
        return encode.matches(password, getAdministratorPassword(userUniqueIdentifier));
    }
    private String getAdministratorPassword(String userUniqueIdentifier) {
        return administratorRepository.findAdministratorByUsername(userUniqueIdentifier).getPassword();
    }
    protected abstract boolean isUserExist(String userUniqueIdentifier);
    private boolean isAdministratorExist(String userUniqueIdentifier) {
        return administratorRepository.findAdministratorByUsername(userUniqueIdentifier) != null;
    }
    protected abstract String getUserPassword(String userUniqueIdentifier);
    private boolean isUserPasswordCorrect(String password, String userUniqueIdentifier) {
        return encode.matches(password, getUserPassword(userUniqueIdentifier));
    }
    protected abstract int getUserId(String userUniqueIdentifier);
}
