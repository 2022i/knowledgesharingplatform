package com.back.sentSystemMessage;

import org.springframework.stereotype.Service;

@Service
public class SentDeleteArticleSuccessMessage extends SentMessage {
    @Override
    protected void setMessageTitle() {
        message.setTitle("DeleteArticleSuccess");
    }
}
