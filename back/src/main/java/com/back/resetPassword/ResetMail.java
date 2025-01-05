package com.back.resetPassword;

import com.back.dto.Response;
import com.back.mail.MailServiceImpl;
import com.back.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ResetMail {
    @Resource
    private UserRepository userRepository;
    @Resource
    private MailServiceImpl mailService;
    public Response mailMail(String email) {
        if(!isEmailExit(email)){
            return Response.notFoundUser();
        }
        mailService.mailSendForForgetPassword(email);
        return Response.successMail();
    }
    private boolean isEmailExit(String email){
        return userRepository.findUserByEmail(email)!=null;
    }
}
