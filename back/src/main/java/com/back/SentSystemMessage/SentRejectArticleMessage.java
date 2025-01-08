package com.back.SentSystemMessage;

import com.back.dto.request.AuditorRequest;
import org.springframework.stereotype.Service;

@Service
public class SentRejectArticleMessage extends SentMessage{
    @Override
    protected void setMessageTitle() {
        message.setTitle("RejectArticle");
    }
    public void sentMessage(AuditorRequest auditorRequest) {
        setMessage(auditorRequest.getArticleId(), auditorRequest.getAuditorId());
        message.setContent(auditorRequest.getReason());
    }
}
