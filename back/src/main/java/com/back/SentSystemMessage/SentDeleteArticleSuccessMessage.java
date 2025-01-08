package com.back.SentSystemMessage;

import org.springframework.stereotype.Service;

@Service
public class SentDeleteArticleSuccessMessage extends SentMessage {
    @Override
    protected void setMessageTitle() {
        message.setTitle("DeleteArticleSuccess");
    }
}
