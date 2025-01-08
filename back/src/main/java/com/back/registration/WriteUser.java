package com.back.registration;

import com.back.get.LastIdOperation;
import com.back.dto.request.RegistrationRequest;
import com.back.dto.Response;
import com.back.index.User;
import com.back.index.UserData;
import com.back.mail.encoding.Encode;
import com.back.redis.RedisCacheServiceImpl;
import com.back.repository.UserDataRepository;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class WriteUser {
    @Resource
    private RedisCacheServiceImpl redisCacheServiceImpl;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    @Resource
    private Encode encode;
    public Response registerByEmail( RegistrationRequest registrationRequest) {
        if(isCodeExpired(registrationRequest.getEmail())){
            return Response.codeExpired();
        }else if(isCodeError(registrationRequest.getCode(), registrationRequest.getEmail())){
            return Response.errorCode();
        }
        writeUser(registrationRequest);
        return Response.successRegister();
    }
    private boolean isCodeExpired(String email){
        return redisCacheServiceImpl.get((email))==null;
    }
    private boolean isCodeError(String code, String email){
        return !code.equals(redisCacheServiceImpl.get(email));
    }
    private void writeUser(RegistrationRequest registrationRequest){
        User user = new User();
        user.setId(lastIdOperation.getUserId());
        user.setEmail(registrationRequest.getEmail());
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(encode.encrypt(registrationRequest.getPassword()));
        userRepository.save(user);
        writeUserData(user);
    }
    private void writeUserData(User user){
        UserData userData = new UserData();
        userData.setId(user.getId());
        userData.setUsername(user.getUsername());
        userDataRepository.save(userData);
    }
}
