package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentSupportMessage extends SentMessage {
    @Override
    public void setMessageTitle() {
        message.setTitle("Support");
    }
}
