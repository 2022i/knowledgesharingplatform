package com.back.mail.send;


import com.back.dto.RequiredInformationForEmail;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.util.Random;

@Data
@Service
public class MessageHelperImp implements MessageHelper {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private RequiredInformationForEmail requiredInformationForEmail;
    @Resource
    private TemplateEngine templateEngine;
    private MimeMessageHelper mimeMessageHelper;
    private MimeMessage mimeMessage;

    @Override
    public void setMessageHelper() throws MessagingException {
        mimeMessage = mailSender.createMimeMessage();
        mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setSubject(requiredInformationForEmail.getSubject());
        mimeMessageHelper.setTo(requiredInformationForEmail.getTo());
        mimeMessageHelper.setFrom(requiredInformationForEmail.getFROM());
    }

    @Override
    public void setRequiredInformation(String to, String subject) {
        requiredInformationForEmail.setTo(to);
        requiredInformationForEmail.setSubject(subject);
        String code = String.valueOf(new Random().nextInt(899999) + 100000);
        requiredInformationForEmail.setCode(code);
    }

    public void send(){
        mailSender.send(mimeMessage);
    }

    public void setText(Context context) throws MessagingException {
        String text = templateEngine.process("email.html", context);
        mimeMessageHelper.setText(text, true);
    }
}
