package com.back.resetPassword;

import com.back.dto.request.ResetPasswordRequest;
import com.back.dto.Response;
import com.back.index.User;
import com.back.mail.encoding.Encode;
import com.back.redis.RedisCacheServiceImpl;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ResetPassword {
    @Resource
    private RedisCacheServiceImpl redisCacheServiceImpl;
    @Resource
    private UserRepository userRepository;
    @Resource
    private Encode encode;
    public Response resetPassword(ResetPasswordRequest resetPasswordRequest) {
        if(isCodeExpired(resetPasswordRequest.getEmail())){
            return Response.codeExpired();
        }else if(isCodeError(resetPasswordRequest.getCode(), resetPasswordRequest.getEmail())){
            return Response.errorCode();
        }
        writeUser(resetPasswordRequest);
        return Response.successResetPassword();
    }
    private boolean isCodeExpired(String email){
        return redisCacheServiceImpl.get((email))==null;
    }
    private boolean isCodeError(String code, String email){
        return !code.equals(redisCacheServiceImpl.get(email));
    }
    private void writeUser(ResetPasswordRequest resetPasswordRequest){
        User user = userRepository.findUserByEmail(resetPasswordRequest.getEmail());
        user.setPassword(encode.encrypt(resetPasswordRequest.getPassword()));
        userRepository.save(user);
    }
}
