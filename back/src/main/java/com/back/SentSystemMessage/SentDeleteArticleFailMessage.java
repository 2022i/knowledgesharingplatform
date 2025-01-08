package com.back.SentSystemMessage;

import com.back.dto.request.AuditorRequest;
import org.springframework.stereotype.Service;

@Service
public class SentDeleteArticleFailMessage extends SentMessage {
    @Override
    protected void setMessageTitle() {
        message.setTitle("DeleteArticleFail");
    }
    public void sentMessage(AuditorRequest auditorRequest) {
        setMessage(auditorRequest.getArticleId(), auditorRequest.getAuditorId());
        message.setContent(auditorRequest.getReason());
    }
}
