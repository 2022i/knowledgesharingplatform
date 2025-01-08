package com.back.SentSystemMessage;

import org.springframework.stereotype.Service;

@Service
public class SentOpposeMessage extends SentMessage {
    @Override
    protected void setMessageTitle() {
        message.setTitle("Oppose");
    }
}
