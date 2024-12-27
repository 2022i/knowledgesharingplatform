package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentShareMessage extends SentMessage {
    @Override
    public void setMessageTitle() {
        message.setTitle("Share");
    }
}
