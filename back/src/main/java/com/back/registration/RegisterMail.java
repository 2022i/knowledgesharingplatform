package com.back.registration;

import com.back.dto.request.RegisterMailRequest;
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
    public Response mailMail(RegisterMailRequest registerMailRequest) {
        if(isUsernameExit(registerMailRequest.getUsername()) ){
            return Response.alreadyExistsEmail();
        }else if(isEmailExit(registerMailRequest.getEmail())){
            return Response.alreadyExistsUsername();
        }
        mailService.mailSendForRegister(registerMailRequest.getEmail());
        return Response.successMail();
    }
    private boolean isEmailExit(String email){
        return userRepository.findUserByEmail(email)!=null;
    }
    private boolean isUsernameExit(String username){
        return userRepository.findUserByUsername(username)!=null;
    }
}
