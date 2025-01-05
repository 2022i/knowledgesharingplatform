package com.back.mail.send;

import jakarta.mail.MessagingException;

public interface MessageHelper {
    void setMessageHelper() throws MessagingException;
    void setRequiredInformation(String to, String subject);
}
