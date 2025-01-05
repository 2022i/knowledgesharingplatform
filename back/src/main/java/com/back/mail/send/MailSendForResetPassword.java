package com.back.mail.send;

import com.back.mail.context.ResetPasswordMailContext;
import org.springframework.stereotype.Service;

@Service
public class MailSendForResetPassword extends AbstractMailSend {
    @Override
    public void setContext() {
        context=new ResetPasswordMailContext();
        context.setContext();
    }

    @Override
    public void setRequiredInformation(String to) {
        messageHelper.setRequiredInformation(to,"项目考核系统重置密码验证码");
    }
}
