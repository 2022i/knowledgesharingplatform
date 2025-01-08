package com.back.sentSystemMessage;

import org.springframework.stereotype.Service;

@Service
public class SentApproveArticleMessage extends SentMessage{
    @Override
    protected void setMessageTitle() {
        message.setTitle("ApproveArticle");
    }
}
