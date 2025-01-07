package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentFailedAuditMessage extends SentMessage{
    @Override
    protected void setMessageTitle() {
        message.setTitle("FailedAudit");
    }
}
