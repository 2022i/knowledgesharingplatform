package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentCollectMessage extends SentMessage {
    @Override
    protected void setMessageTitle() {
        message.setTitle("Collect");
    }
}
