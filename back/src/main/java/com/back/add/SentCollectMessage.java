package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentCollectMessage extends SentMessage {
    @Override
    public void setMessageTitle() {
        message.setTitle("Collect");
    }
}
