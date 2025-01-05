package com.back.mail.send;

import com.back.mail.context.RegistrationMailContext;
import org.springframework.stereotype.Service;

@Service
public class MailSendForRegister extends AbstractMailSend {
    @Override
    public void setContext() {
        context=new RegistrationMailContext();
        context.setContext();
    }

    @Override
    public void setRequiredInformation(String to) {
        messageHelper.setRequiredInformation(to,"项目考核系统注册验证码");
    }
}
