package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentShareMessage extends SentMessage {
    @Override
    protected void setMessageTitle() {
        message.setTitle("Share");
    }
}
