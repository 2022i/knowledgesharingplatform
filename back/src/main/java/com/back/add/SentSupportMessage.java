package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentSupportMessage extends SentMessage {
    @Override
    protected void setMessageTitle() {
        message.setTitle("Support");
    }
}
