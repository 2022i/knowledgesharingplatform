package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentArticleCommentMessage extends SentMessage{
    @Override
    protected void setMessageTitle() {
        message.setTitle("ArticleComment");
    }
}
