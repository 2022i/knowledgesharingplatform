package com.back.registration;

import com.back.dto.request.RegisterMailRequest;
import com.back.dto.Response;
import com.back.mail.MailServiceImpl;
import com.back.repository.AdministratorRepository;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RegisterMail {
    @Resource
    private UserRepository userRepository;
    @Resource
    private MailServiceImpl mailService;
    @Resource
    private AdministratorRepository administratorRepository;
    public Response mailMail(RegisterMailRequest registerMailRequest) {
        if(isUsernameExit(registerMailRequest.getUsername())){
            return Response.alreadyExistsUsername();
        }else if(isEmailExit(registerMailRequest.getEmail())){
            return Response.alreadyExistsEmail();
        }
        mailService.mailSendForRegister(registerMailRequest.getEmail());
        return Response.successMail();
    }
    private boolean isEmailExit(String email){
        return userRepository.findUserByEmail(email)!=null;
    }
    private boolean isUsernameExit(String username){
        return userRepository.findUserByUsername(username)!=null||administratorRepository.findAdministratorByUsername(username)!=null;
    }
}
