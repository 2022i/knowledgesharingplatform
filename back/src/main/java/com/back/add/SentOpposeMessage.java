package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentOpposeMessage extends SentMessage {
    @Override
    public void setMessageTitle() {
        message.setTitle("Oppose");
    }
}
