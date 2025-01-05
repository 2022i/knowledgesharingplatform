package com.back.registration;

import com.back.dto.RegisterUserData;
import com.back.dto.Response;
import com.back.mail.MailServiceImpl;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RegisterMail {
    @Resource
    private UserRepository userRepository;
    @Resource
    private MailServiceImpl mailService;
    public Response mailMail(RegisterUserData registerUserData) {
        if(isUsernameExit(registerUserData.getUsername()) ){
            return Response.alreadyExistsEmail();
        }else if(isEmailExit(registerUserData.getEmail())){
            return Response.alreadyExistsUsername();
        }
        mailService.mailSendForRegister(registerUserData.getEmail());
        return Response.successMail();
    }
    private boolean isEmailExit(String email){
        return userRepository.findUserByEmail(email)!=null;
    }
    private boolean isUsernameExit(String username){
        return userRepository.findUserByUsername(username)!=null;
    }
}
