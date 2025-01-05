package com.back.mail;

public interface MailService {
    void sendMail(String to, String subject);
    void mailSendForRegister(String to);
    void mailSendForForgetPassword(String to);
    void setContext(String subject);
    String getCode();
}
